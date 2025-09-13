package de.geosphere.speechplaning.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import de.geosphere.speechplaning.data.model.District
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
class DistrictRepositoryTest {

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

    private lateinit var districtRepository: DistrictRepository

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

        districtRepository = DistrictRepository(firestoreMock)
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
    fun `extractIdFromEntity should return correct id`() {
        val district = District(id = "testId", name = "Test District")
        val extractedId = districtRepository.extractIdFromEntity(district)
        assertEquals("testId", extractedId)
    }

    @Test
    fun `save new district should add to firestore and return new id`() = runTest {
        val newDistrict = District(id = "", circuitOverseerId = "1", name = "Max Mustermann", active = true)
        val generatedId = "generatedFirebaseId"
        val addedDocRefMock: DocumentReference = mockk() // separater Mock für das Ergebnis von add()

        mockTaskResult(documentReferenceTaskMock, addedDocRefMock)
        every { addedDocRefMock.id } returns generatedId

        val resultId = districtRepository.save(newDistrict)

        verify { collectionReferenceMock.add(newDistrict) }
        assertEquals(generatedId, resultId)
    }

    @Test
    fun `save existing district should set document in firestore and return existing id`() = runTest {
        val existingDistrict = District(
            id = "existingId",
            circuitOverseerId = "2",
            name = "Max Mustermann",
            active = true
        )
        mockTaskResult(voidTaskMock, null) // Für Task<Void> ist das Ergebnis null

        val resultId = districtRepository.save(existingDistrict)

        verify { collectionReferenceMock.document("existingId") }
        verify { documentReferenceMock.set(existingDistrict) }
        assertEquals("existingId", resultId)
    }

    @Test
    fun `getDistrict with valid id should return district object`() = runTest {
        val districtId = "district123"
        val expectedDistrict = District(
            id = districtId,
            circuitOverseerId = "2",
            name = "Max Mustermann",
            active = true
        )
        val snapshotResultMock: DocumentSnapshot = mockk()

        mockTaskResult(documentSnapshotTaskMock, snapshotResultMock)
        every { snapshotResultMock.exists() } returns true
        every { snapshotResultMock.toObject(District::class.java) } returns expectedDistrict
        // Sicherstellen, dass der richtige docRef für get verwendet wird:
        every { collectionReferenceMock.document(districtId) } returns documentReferenceMock
        every { documentReferenceMock.get() } returns documentSnapshotTaskMock


        val result = districtRepository.getById(districtId)

        assertNotNull(result)
        assertEquals(expectedDistrict, result)
        verify { collectionReferenceMock.document(districtId) }
    }

    @Test
    fun `getDistrict with invalid id should return null`() = runTest {
        val districtId = "nonExistingId"
        val snapshotResultMock: DocumentSnapshot = mockk()

        mockTaskResult(documentSnapshotTaskMock, snapshotResultMock)
        every { snapshotResultMock.exists() } returns false
        every { snapshotResultMock.toObject(District::class.java) } returns null
        every { collectionReferenceMock.document(districtId) } returns documentReferenceMock
        every { documentReferenceMock.get() } returns documentSnapshotTaskMock

        val result = districtRepository.getById(districtId)

        assertNull(result)
        verify { collectionReferenceMock.document(districtId) }
    }

    @Test
    fun `getAllDistricts should return list of districts`() = runTest {
        val district1 = District(id = "id1", circuitOverseerId = "1", name = "Max Mustermann", active = true)
        val district2 = District(id = "id2", circuitOverseerId = "1", name = "Frau Mustermann", active = true)
        val querySnapshotResultMock: QuerySnapshot = mockk()

        mockTaskResult(querySnapshotTaskMock, querySnapshotResultMock)
        // every { querySnapshotResultMock.documents } returns documents // toObjects ist meist einfacher
        every { querySnapshotResultMock.toObjects(District::class.java) } returns listOf(district1, district2)
        // Sicherstellen, dass get() auf der Collection Mock das querySnapshotTaskMock zurückgibt
        every { collectionReferenceMock.get() } returns querySnapshotTaskMock


        val result = districtRepository.getAll()

        assertEquals(2, result.size)
        assertTrue(result.containsAll(listOf(district1, district2)))
        verify { collectionReferenceMock.get() }
    }


    @Test
    fun `deleteDistrict should call delete on document`() = runTest {
        val districtId = "districtToDelete"
        mockTaskResult(voidTaskMock, null)
        every { collectionReferenceMock.document(districtId) } returns documentReferenceMock
        every { documentReferenceMock.delete() } returns voidTaskMock


        districtRepository.delete(districtId)

        verify { collectionReferenceMock.document(districtId) }
        verify { documentReferenceMock.delete() }
    }

    @Test
    fun `getActiveDistricts should query and return active speeches`() = runTest {
        val activeDistrict = District(id = "active1", circuitOverseerId = "1", name = "Max Mustermann", active = true)
        // val inactiveSpeech = Speech(id = "inactive1", subject = "Inactive Speech", active = false)
        // Nicht benötigt, da Query serverseitig filtert
        val querySnapshotResultMock: QuerySnapshot = mockk()

        mockTaskResult(querySnapshotTaskMock, querySnapshotResultMock)
        every { querySnapshotResultMock.toObjects(District::class.java) } returns listOf(activeDistrict)

        every { collectionReferenceMock.whereEqualTo("active", true) } returns queryMock
        every { queryMock.get() } returns querySnapshotTaskMock

        val result = districtRepository.getActiveDistricts()

        assertEquals(1, result.size)
        assertEquals(activeDistrict, result[0])
        verify { collectionReferenceMock.whereEqualTo("active", true) }
        verify { queryMock.get() }
    }

    @Test
    fun `save new district should throw runtime exception on firestore failure`() = runTest {
        val newDistrict = District(id = "", circuitOverseerId = "1", name = "Max Mustermann", active = true)
        val simpleException = RuntimeException("Simulated Firestore error")

        // Task schlägt fehl
        mockTaskResult(documentReferenceTaskMock, null, simpleException)
        // Sicherstellen, dass add mit newSpeech den fehlschlagenden Task zurückgibt
        every { collectionReferenceMock.add(newDistrict) } returns documentReferenceTaskMock


        val exception = assertThrows<RuntimeException> {
            districtRepository.save(newDistrict)
        }
        assertTrue(exception.message?.contains("Failed to save entity [new] in districts") ?: false)
        assertEquals(simpleException, exception.cause)
    }

    @Test
    fun `getActiveDistricts should throw runtime exception on firestore failure`() = runTest {
        val simulatedException = RuntimeException("Simulated Firestore error")

        // Mock the query chain to return a failing task
        every { collectionReferenceMock.whereEqualTo("active", true) } returns queryMock
        every { queryMock.get() } returns querySnapshotTaskMock
        mockTaskResult(querySnapshotTaskMock, null, simulatedException)

        val exception = assertThrows<RuntimeException> {
            districtRepository.getActiveDistricts()
        }

        assertTrue(exception.message?.contains("Failed to get active district from districts") ?: false)
        assertEquals(simulatedException, exception.cause)
        verify { collectionReferenceMock.whereEqualTo("active", true) }
        verify { queryMock.get() }
    }
}
