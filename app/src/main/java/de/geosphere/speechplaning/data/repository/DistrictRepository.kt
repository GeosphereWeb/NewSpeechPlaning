package de.geosphere.speechplaning.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import de.geosphere.speechplaning.data.model.District
import de.geosphere.speechplaning.data.repository.base.BaseFirestoreRepository
import kotlinx.coroutines.tasks.await

private const val DISTRICTS_COLLECTION = "districts"

@Suppress("TooGenericExceptionCaught", "TooGenericExceptionThrown")
@SuppressWarnings("kotlin:S3776")
class DistrictRepository(firestore: FirebaseFirestore) : BaseFirestoreRepository<District>(
    firestore = firestore,
    collectionPath = DISTRICTS_COLLECTION,
    clazz = District::class.java
) {
    override fun extractIdFromEntity(entity: District): String {
        return entity.id
    }

    /**
     * Ruft alle aktiven Ditricts ab.
     * Dies verwendet eine serverseitige Query.
     * Diese Methode ist spezifisch für SpeechRepository und nicht Teil der Basisklasse.
     *
     * @return Eine Liste von aktiven Speech-Objekten.
     */
    suspend fun getActiveDistricts(): List<District> {
        return try {
            val querySnapshot = firestore.collection(DISTRICTS_COLLECTION)
                .whereEqualTo("active", true)
                .get()
                .await()
            querySnapshot.toObjects(District::class.java)
        } catch (e: Exception) {
            throw RuntimeException("Failed to get active district from $DISTRICTS_COLLECTION", e)
        }
    }
}
