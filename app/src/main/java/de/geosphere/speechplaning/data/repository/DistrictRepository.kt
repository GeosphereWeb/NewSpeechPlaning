package de.geosphere.speechplaning.data.repository

import android.util.Log
import de.geosphere.speechplaning.data.model.District
import de.geosphere.speechplaning.data.services.FirestoreService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class DistrictRepository(firestoreService: FirestoreService) {
    val clazz = District::class.java
    private val collection = firestoreService.getCollection(clazz)


    /**
     * Retrieves all districts from Firestore and listens for real-time updates.
     *
     * This function returns a [Flow] that emits a list of [District] objects.
     * The Flow will emit an initial list of districts and then continue to emit
     * new lists whenever the data changes in the Firestore collection.
     *
     * If an error occurs while fetching or listening for updates, the Flow will be
     * closed with the corresponding error.
     *
     * The listener to Firestore is automatically removed when the Flow is cancelled
     * or completed.
     *
     * @return A [Flow] of [List<District>] representing the districts in Firestore.
     */
    fun getDistricts() : Flow<List<District>> = callbackFlow {
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

    /**
     * Ruft einen einzelnen Sprecher anhand seiner ID aus Firestore ab.
     * Gibt einen Flow zurück, der den Sprecher oder null (wenn nicht gefunden) emittiert
     * und auf Änderungen am Dokument lauscht.
     */
    fun getDistrictById(districtId: String): Flow<District?> = callbackFlow {
        if (districtId.isBlank()) {
            trySend(null) // Ungültige oder leere ID sofort mit null beantworten
            close()
            return@callbackFlow
        }

        val documentReference = collection.document(districtId)

        val listenerRegistration = documentReference.addSnapshotListener { documentSnapshot, error ->
            if (error != null) {
                Log.w("SpeakerRepositoryImpl", "Error listening for speaker document $districtId updates", error)
                close(error) // Schließe den Flow mit einem Fehler
                return@addSnapshotListener
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                try {
                    val district = documentSnapshot.toObject(clazz)
                    trySend(district) // Sende den gefundenen/aktualisierten Sprecher
                } catch (e: Exception) {
                    Log.e("DistrictRepository", "Error deserializing speaker document $districtId", e)
                    // Optional: Flow mit Fehler schließen oder null senden, wenn Deserialisierung fehlschlägt
                    trySend(null) // Oder close(e)
                }
            } else {
                trySend(null) // Dokument nicht gefunden oder existiert nicht mehr
            }
        }

        // Wird aufgerufen, wenn der Flow vom Collector geschlossen/abgebrochen wird
        awaitClose {
            Log.d("DistrictRepository", "Closing listener for speaker document $districtId.")
            listenerRegistration.remove()
        }
    }

    /**
     * Löscht einen District anhand seiner ID aus Firestore.
     *
     * @param id Die ID des zu löschenden Districts.
     * @return Die ID des gelöschten Districts bei Erfolg.
     * @throws RuntimeException wenn beim Löschen ein Fehler auftritt (außer bei Abbruch der Coroutine).
     * @throws CancellationException wenn die Coroutine während des Löschvorgangs abgebrochen wird.
     */
    suspend fun deleteDistrict(id: String): String {
        return try {
            collection.document(id).delete().await()
            id
        } catch (e: CancellationException) {
            Log.e("DistrictRepository", "Delete District - Coroutine cancelled", e)
            throw e // Re-throw cancellation exceptions
        } catch (e: Exception) {
            // Logge den spezifischen Fehler und wirf ggf. eine spezifischere Exception
            Log.e("DistrictRepository", "Error deleting district $id", e)
            throw RuntimeException("Failed to delete district $id", e)
        }
    }

    /**
     * Speichert einen neuen District in Firestore oder aktualisiert einen bestehenden District.
     *
     * Wenn die `id` des übergebenen `district`-Objekts leer ist (`isBlank()`),
     * wird ein neuer District in der Firestore-Sammlung angelegt und die automatisch
     * generierte Dokumenten-ID zurückgegeben.
     *
     * Wenn die `id` des `district`-Objekts nicht leer ist, wird versucht, das
     * bestehende Dokument mit dieser ID zu aktualisieren.
     *
     * @param district Das [District]-Objekt, das gespeichert oder aktualisiert werden soll.
     * @return Die ID des gespeicherten oder aktualisierten Districts.
     * @throws RuntimeException wenn beim Speichern ein Fehler auftritt (außer bei Abbruch der Coroutine).
     * @throws CancellationException wenn die Coroutine während des Speichervorgangs abgebrochen wird.
     */
    suspend fun saveDistrict(district: District): String {
        return try {
            if (district.id.isBlank()) {
                // Neuer District: Firestore generiert eine ID
                val documentReference = collection.add(district).await()
                documentReference.id // Gib die von Firestore generierte ID zurück
            } else {
                // Bestehender District: Aktualisiere das Dokument mit der vorhandenen ID
                collection.document(district.id).set(district).await()
                district.id // Gib die bestehende ID zurück
            }
        } catch (e: CancellationException) {
            Log.e("DistrictRepository", "Save District - Coroutine cancelled", e)
            throw e // Re-throw cancellation exceptions
        } catch (e: Exception) {
            Log.e("DistrictRepository", "Error saving district ${district.id}", e)
            throw RuntimeException("Failed to save district ${district.id}", e)
        }
    }

    /**
     * Sets the active status of a district in Firestore.
     *
     * @param districtId The ID of the district to update.
     * @param active The new active status for the district.
     * @throws RuntimeException if an error occurs during the update (except for coroutine cancellation).
     * @throws CancellationException if the coroutine is cancelled during the update.
     */
    suspend fun setDistrictActive(districtId: String, active: Boolean) {
        collection.document(districtId).update("active", active).await()
    }
}