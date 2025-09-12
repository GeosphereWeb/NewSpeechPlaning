package de.geosphere.speechplaning.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import de.geosphere.speechplaning.data.model.Speech
import de.geosphere.speechplaning.data.repository.base.BaseFirestoreRepository
import kotlinx.coroutines.tasks.await

private const val SPEECHES_COLLECTION = "speeches"

@Suppress("TooGenericExceptionCaught", "TooGenericExceptionThrown")
class SpeechRepository(
    firestore: FirebaseFirestore
) : BaseFirestoreRepository<Speech>(
    firestore = firestore,
    collectionPath = SPEECHES_COLLECTION,
    clazz = Speech::class.java
) {

    // Implementierung der abstrakten Methode aus BaseFirestoreRepository
    override fun extractIdFromEntity(entity: Speech): String {
        return entity.id
    }

    /**
     * Ruft alle aktiven Reden ab.
     * Dies verwendet eine serverseitige Query.
     * Diese Methode ist spezifisch für SpeechRepository und nicht Teil der Basisklasse.
     *
     * @return Eine Liste von aktiven Speech-Objekten.
     */
    suspend fun getActiveSpeeches(): List<Speech> {
        return try {
            val querySnapshot = firestore.collection(SPEECHES_COLLECTION)
                .whereEqualTo("active", true)
                .get()
                .await()
            querySnapshot.toObjects(Speech::class.java)
        } catch (e: Exception) {
            throw RuntimeException("Failed to get active speeches from $SPEECHES_COLLECTION", e)
        }
    }
}
