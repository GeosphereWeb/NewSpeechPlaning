package de.geosphere.speechplaning.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestoreException
import de.geosphere.speechplaning.data.model.LecturePlanning
import de.geosphere.speechplaning.data.services.FirestoreService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class LecturePlanningRepository(private val firestoreService: FirestoreService) {
    val clazz = LecturePlanning::class.java
    private val collection = firestoreService.getCollection(clazz)

    fun getLecturePlannings(): Flow<List<LecturePlanning>> = callbackFlow {
        val listenerRegistration = collection
            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    close(error) // Schließe den Flow mit einem Fehler
                    return@addSnapshotListener
                }

                val districts = querySnapshot?.documents?.mapNotNull { it.toObject(clazz) }
                    ?: emptyList()
                trySend(districts) // Sende die aktualisierte Liste
            }
        awaitClose { listenerRegistration.remove() } // Entferne den Listener beim Schließen des Flows
    }

    @Suppress("TooGenericExceptionCaught")
    suspend fun saveDistrict(lecturePlanning: LecturePlanning): String {
        return try {
            if (lecturePlanning.id.nullOrEmpty()) {
                // Neuer District: Firestore generiert eine ID
                val documentReference = collection.add(lecturePlanning).await()
                documentReference.id // Gib die von Firestore generierte ID zurück
            } else {
                // Bestehender District: Aktualisiere das Dokument mit der vorhandenen ID
                collection.document(lecturePlanning.id).set(lecturePlanning).await()
                lecturePlanning.id // Gib die bestehende ID zurück
            }
        } catch (e: CancellationException) {
            Log.e("DistrictRepository", "Save District - Coroutine cancelled", e)
            throw e
        } catch (e: FirebaseFirestoreException) { // Spezifischere Exception für Firestore-Fehler
            Log.e("DistrictRepository", "Error saving district ${lecturePlanning.id}", e)
            throw DistrictRepositoryException("Failed to save district ${lecturePlanning.id}", e)
        } catch (e: Exception) { // Fallback für unerwartete Fehler
            Log.e("DistrictRepository", "Unexpected error saving district ${lecturePlanning.id}", e)
            throw DistrictRepositoryException("An unexpected error occurred while saving district ${lecturePlanning.id}", e)
        }
    }
}
