package de.geosphere.speechplaning.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import de.geosphere.speechplaning.data.model.Speech
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertNull
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class SpeechRepositoryTest {

    @MockK
    private lateinit var firestoreMock: FirebaseFirestore

    @MockK
    private lateinit var collectionReferenceMock: CollectionReference

    @MockK
    private lateinit var documentReferenceMock: DocumentReference

    @MockK
    private lateinit var queryMock: Query

    // Tasks Mocks - Using relaxed = true to avoid mocking all Task methods
    @MockK(relaxed = true)
    private lateinit var voidTaskMock: Task<Void>
    @MockK(relaxed = true)
    private lateinit var documentReferenceTaskMock: Task<DocumentReference>

    @MockK(relaxed = true)
    private lateinit var querySnapshotTaskMock: Task<QuerySnapshot>

    @MockK(relaxed = true)
    private lateinit var documentSnapshotTaskMock: Task<DocumentSnapshot>

    private lateinit var speechRepository: SpeechRepository

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this) // Initialize @MockK annotated mocks

        every { firestoreMock.collection(any()) } returns collectionReferenceMock
        every { collectionReferenceMock.document(any()) } returns documentReferenceMock
        every { collectionReferenceMock.add(any()) } returns documentReferenceTaskMock
        every { documentReferenceMock.set(any()) } returns voidTaskMock
        every { documentReferenceMock.get() } returns documentSnapshotTaskMock
        every { documentReferenceMock.delete() } returns voidTaskMock
        every { collectionReferenceMock.get() } returns querySnapshotTaskMock
        every { collectionReferenceMock.whereEqualTo(any<String>(), any()) } returns queryMock
        every { queryMock.get() } returns querySnapshotTaskMock

        speechRepository = SpeechRepository(firestoreMock)
    }

    // Helper zum einfachen Mocken von Task-Ergebnissen für await()
    private fun <T> mockTaskResult(task: Task<T>, resultData: T?, exception: Exception? = null) {
        every { task.isComplete } returns true
        every { task.isCanceled } returns false
        if (exception != null) {
            every { task.isSuccessful } returns false
            every { task.exception } returns exception
        } else {
            every { task.isSuccessful } returns true
            every { task.exception } returns null
            every { task.result } returns resultData
        }
    }


    @Test
    fun `save new speech should add to firestore and return new id`() = runTest {
        val newSpeech = Speech(id = "", number = "1", subject = "New Subject")
        val generatedId = "generatedFirebaseId"
        val addedDocRefMock: DocumentReference = mockk() // separater Mock für das Ergebnis von add()

        mockTaskResult(documentReferenceTaskMock, addedDocRefMock)
        every { addedDocRefMock.id } returns generatedId
        // Präziseres Matching für add, wenn nötig:
        // every { collectionReferenceMock.add(newSpeech) } returns documentReferenceTaskMock

        val resultId = speechRepository.save(newSpeech)

        verify { collectionReferenceMock.add(newSpeech) }
        assertEquals(generatedId, resultId)
    }

    @Test
    fun `save existing speech should set document in firestore and return existing id`() = runTest {
        val existingSpeech = Speech(id = "existingId", number = "2", subject = "Existing Subject")
        mockTaskResult(voidTaskMock, null) // Für Task<Void> ist das Ergebnis null

        val resultId = speechRepository.save(existingSpeech)

        verify { collectionReferenceMock.document("existingId") }
        verify { documentReferenceMock.set(existingSpeech) }
        assertEquals("existingId", resultId)
    }

    @Test
    fun `getSpeech with valid id should return speech object`() = runTest {
        val speechId = "speech123"
        val expectedSpeech = Speech(id = speechId, number = "3", subject = "Test Speech")
        val snapshotResultMock: DocumentSnapshot = mockk()

        mockTaskResult(documentSnapshotTaskMock, snapshotResultMock)
        every { snapshotResultMock.exists() } returns true
        every { snapshotResultMock.toObject(Speech::class.java) } returns expectedSpeech
        // Sicherstellen, dass der richtige docRef für get verwendet wird:
        every { collectionReferenceMock.document(speechId) } returns documentReferenceMock
        every { documentReferenceMock.get() } returns documentSnapshotTaskMock


        val result = speechRepository.getById(speechId)

        assertNotNull(result)
        assertEquals(expectedSpeech, result)
        verify { collectionReferenceMock.document(speechId) }
    }

    @Test
    fun `getSpeech with invalid id should return null`() = runTest {
        val speechId = "nonExistingId"
        val snapshotResultMock: DocumentSnapshot = mockk()

        mockTaskResult(documentSnapshotTaskMock, snapshotResultMock)
        every { snapshotResultMock.exists() } returns false
        every { snapshotResultMock.toObject(Speech::class.java) } returns null
        every { collectionReferenceMock.document(speechId) } returns documentReferenceMock
        every { documentReferenceMock.get() } returns documentSnapshotTaskMock

        val result = speechRepository.getById(speechId)

        assertNull(result)
        verify { collectionReferenceMock.document(speechId) }
    }

    @Test
    fun `getAllSpeeches should return list of speeches`() = runTest {
        val speech1 = Speech(id = "id1", subject = "Subject 1")
        val speech2 = Speech(id = "id2", subject = "Subject 2")
        val querySnapshotResultMock: QuerySnapshot = mockk()

        mockTaskResult(querySnapshotTaskMock, querySnapshotResultMock)
        // every { querySnapshotResultMock.documents } returns documents // toObjects ist meist einfacher
        every { querySnapshotResultMock.toObjects(Speech::class.java) } returns listOf(speech1, speech2)
        // Sicherstellen, dass get() auf der Collection Mock das querySnapshotTaskMock zurückgibt
        every { collectionReferenceMock.get() } returns querySnapshotTaskMock


        val result = speechRepository.getAll()

        assertEquals(2, result.size)
        assertTrue(result.containsAll(listOf(speech1, speech2)))
        verify { collectionReferenceMock.get() }
    }


    @Test
    fun `deleteSpeech should call delete on document`() = runTest {
        val speechId = "speechToDelete"
        mockTaskResult(voidTaskMock, null)
        every { collectionReferenceMock.document(speechId) } returns documentReferenceMock
        every { documentReferenceMock.delete() } returns voidTaskMock


        speechRepository.delete(speechId)

        verify { collectionReferenceMock.document(speechId) }
        verify { documentReferenceMock.delete() }
    }

    @Test
    fun `getActiveSpeeches should query and return active speeches`() = runTest {
        val activeSpeech = Speech(id = "active1", subject = "Active Speech", active = true)
        // val inactiveSpeech = Speech(id = "inactive1", subject = "Inactive Speech", active = false)
        // Nicht benötigt, da Query serverseitig filtert
        val querySnapshotResultMock: QuerySnapshot = mockk()

        mockTaskResult(querySnapshotTaskMock, querySnapshotResultMock)
        every { querySnapshotResultMock.toObjects(Speech::class.java) } returns listOf(activeSpeech)

        every { collectionReferenceMock.whereEqualTo("active", true) } returns queryMock
        every { queryMock.get() } returns querySnapshotTaskMock

        val result = speechRepository.getActiveSpeeches()

        assertEquals(1, result.size)
        assertEquals(activeSpeech, result[0])
        verify { collectionReferenceMock.whereEqualTo("active", true) }
        verify { queryMock.get() }
    }

    @Test
    fun `save new speech should throw runtime exception on firestore failure`() = runTest {
        val newSpeech = Speech(id = "", number = "1", subject = "New Subject")
        val simpleException = RuntimeException("Simulated Firestore error")

        // Task schlägt fehl
        mockTaskResult(documentReferenceTaskMock, null, simpleException)
        // Sicherstellen, dass add mit newSpeech den fehlschlagenden Task zurückgibt
        every { collectionReferenceMock.add(newSpeech) } returns documentReferenceTaskMock


        val exception = assertThrows<RuntimeException> {
            speechRepository.save(newSpeech)
        }
        assertTrue(exception.message?.contains("Failed to save entity [new] in speeches") ?: false)
        assertEquals(simpleException, exception.cause)
    }
}
