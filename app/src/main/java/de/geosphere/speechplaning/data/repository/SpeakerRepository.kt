package de.geosphere.speechplaning.data.repository

import android.util.Log
import de.geosphere.speechplaning.data.model.Speaker
import de.geosphere.speechplaning.data.services.FirestoreService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SpeakerRepository(private val firestoreService: FirestoreService) : ISpeakerRepository {
    private val collection = firestoreService.getCollection(Speaker::class.java)

    override suspend fun saveSpeaker(congregationId: String, speaker: Speaker): String {
        if (congregationId.isBlank()) {
            throw IllegalArgumentException("Congregation ID cannot be blank to save a speaker.")
        }

        val speakerToSave = if (speaker.speakerId.isBlank()) {
            speaker.copy(speakerId = firestoreService.getSpeakersSubcollection(congregationId).document().id)
        } else {
            speaker
        }

        // Verwende die neue Funktion, um die Subcollection-Referenz zu bekommen
        firestoreService.getSpeakersSubcollection(congregationId)
            .document(speakerToSave.speakerId) // Verwende die (ggf. neu generierte) Speaker-ID
            .set(speakerToSave) // 'set' zum Erstellen oder Überschreiben
            .await()
        Log.d("SpeakerRepository", "Speaker ${speakerToSave.speakerId} saved for congregation $congregationId")
        return speakerToSave.speakerId
    }

    override suspend fun getSpeakersForCongregation(congregationId: String): List<Speaker> {
        if (congregationId.isBlank()) return emptyList()
        return try {
            // Verwende die neue Funktion
            val querySnapshot = firestoreService.getSpeakersSubcollection(congregationId).get().await()
            querySnapshot.toObjects(Speaker::class.java)
        } catch (e: Exception) {
            Log.e("SpeakerRepository", "Error fetching speakers for congregation $congregationId", e)
            emptyList()
        }
    }

    /**
     * Beobachtet die Liste der Speaker einer Congregation auf Änderungen in Echtzeit.
     * Gibt einen Flow zurück, der die Liste der Speaker-Objekte emittiert.
     */
    override fun getSpeakersForCongregationFlow(congregationId: String): Flow<List<Speaker>> {
        if (congregationId.isBlank()) {
            // throw IllegalArgumentException("Congregation ID cannot be blank for flow.")
            return flowOf(emptyList()) // Sicherer für combine, wenn eine ID leer ist
        }
        return callbackFlow {
            val collectionRef = firestoreService.getSpeakersSubcollection(congregationId)
            val listenerRegistration = collectionRef.addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.w("SpeakerRepository", "Listen failed for speakers in $congregationId.", error)
                    trySend(emptyList())
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshots != null) {
                    Log.d(
                        "SpeakerRepository",
                        "Speakers for $congregationId updated. Count: ${
                            snapshots.toObjects(
                                Speaker::class.java
                            ).size}"
                    )
                    trySend(snapshots.toObjects(Speaker::class.java)).isSuccess
                } else {
                    trySend(emptyList()).isSuccess // Sollte nicht passieren, wenn error null ist
                }
            }

            awaitClose {
                Log.d("SpeakerRepository", "Closing snapshot listener for speakers in $congregationId.")
                listenerRegistration.remove()
            }
        }
    }

    // ... andere Speaker-Methoden ...
    override suspend fun getSpeaker(congregationId: String, speakerId: String): Speaker? {
        if (congregationId.isBlank() || speakerId.isBlank()) return null
        return try {
            firestoreService.getSpeakersSubcollection(congregationId)
                .document(speakerId).get().await()
                .toObject(Speaker::class.java)
        } catch (e: Exception) {
            Log.e("SpeakerRepository", "Error fetching speaker $speakerId from congregation $congregationId", e)
            null
        }
    }

    /**
     * Beobachtet ein einzelnes Speaker-Dokument auf Änderungen in Echtzeit.
     * Gibt einen Flow zurück, der das Speaker-Objekt oder null (wenn nicht vorhanden/Fehler) emittiert.
     */
    override fun getSpeakerFlow(congregationId: String, speakerId: String): Flow<Speaker?> {
        if (congregationId.isBlank() || speakerId.isBlank()) {
            // Du könntest hier einen Flow zurückgeben, der sofort einen Fehler oder null emittiert,
            // oder eine Exception werfen, je nach deiner Fehlerbehandlungsstrategie.
            // Für dieses Beispiel werfen wir eine Exception, die im ViewModel gefangen werden kann.
            throw IllegalArgumentException("Congregation ID or Speaker ID cannot be blank for flow.")
        }
        return callbackFlow {
            // Referenz zum Speaker-Dokument
            val docRef = firestoreService.getSpeakersSubcollection(congregationId)
                .document(speakerId)

            // Snapshot-Listener registrieren
            val listenerRegistration = docRef.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w("SpeakerRepository", "Listen failed for speaker $speakerId in $congregationId.", error)
                    trySend(null) // Oder channel.close(error) um den Flow mit Fehler zu beenden
                    close(error) // Den Flow bei einem Fehler schließen
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val speaker = snapshot.toObject(Speaker::class.java)
                    Log.d("SpeakerRepository", "Speaker $speakerId updated: $speaker")
                    trySend(speaker).isSuccess // Versuche, das Speaker-Objekt zu senden
                } else {
                    Log.d("SpeakerRepository", "Speaker $speakerId does not exist or was deleted.")
                    trySend(null).isSuccess // Sende null, wenn das Dokument nicht existiert
                }
            }

            // Wenn der Flow vom Collector geschlossen wird (z.B. weil der Scope des ViewModels endet),
            // wird der Listener entfernt, um Ressourcen freizugeben und Memory Leaks zu vermeiden.
            awaitClose {
                Log.d("SpeakerRepository", "Closing snapshot listener for speaker $speakerId in $congregationId.")
                listenerRegistration.remove()
            }
        }
    }

    /**
     * Beobachtet die Speaker von mehreren spezifischen Congregation-IDs in Echtzeit.
     * Kombiniert die Ergebnisse zu einer einzigen Liste.
     */
    @OptIn(ExperimentalCoroutinesApi::class) // Für `combine` mit variabler Anzahl an Flows
    override fun getSpeakersForMultipleCongregationsFlow(congregationIdsFlow: Flow<List<String>>): Flow<List<Speaker>> {
        // flatMapLatest wird verwendet, falls sich die Liste der zu beobachtenden congregationIds
        // selbst im Laufe der Zeit ändern kann (z.B. basierend auf Benutzereinstellungen).
        // Wenn die Liste der IDs statisch ist, nachdem der Flow gestartet wurde, ist es einfacher.
        return congregationIdsFlow.flatMapLatest { ids ->
            if (ids.isEmpty()) {
                flowOf(emptyList()) // Keine IDs, also leere Speaker-Liste
            } else {
                // Erstelle einen Flow für jede Congregation-ID
                val speakerFlows: List<Flow<List<Speaker>>> = ids.map { congId ->
                    getSpeakersForCongregationFlow(congId)
                        .catch { e -> // Fehlerbehandlung pro innerem Flow
                            Log.e("SpeakerRepo", "Error in flow for congId $congId", e)
                            emit(emptyList()) // Bei Fehler eine leere Liste für diesen Flow emittieren
                        }
                }

                // Kombiniere die Ergebnisse aller Flows.
                // Immer wenn einer der Flows einen neuen Wert emittiert, wird der combine-Block
                // mit den neuesten Werten aller Flows ausgeführt.
                combine(speakerFlows) { arrayOfSpeakerLists ->
                    // arrayOfSpeakerLists ist ein Array von List<Speaker>
                    // Wir müssen sie zu einer einzigen List<Speaker> zusammenführen
                    arrayOfSpeakerLists.toList().flatten()
                }
            }
        }
    }

    // Alternative, wenn die Liste der congregationIds beim Start des Flows feststeht:
    override fun getSpeakersForFixedMultipleCongregationsFlow(congregationIds: List<String>): Flow<List<Speaker>> {
        if (congregationIds.isEmpty()) {
            return flowOf(emptyList())
        }

        val speakerFlows: List<Flow<List<Speaker>>> = congregationIds.map { congId ->
            getSpeakersForCongregationFlow(congId)
                .catch { e -> emit(emptyList()) }
        }

        return combine(speakerFlows) { arrayOfSpeakerLists ->
            arrayOfSpeakerLists.toList().flatten()
        }
    }

    override suspend fun deleteSpeaker(congregationId: String, speakerId: String) {
        if (congregationId.isBlank() || speakerId.isBlank()) return
        try {
            firestoreService.getSpeakersSubcollection(congregationId)
                .document(speakerId).delete().await()
            Log.d("SpeakerRepository", "Speaker $speakerId deleted from congregation $congregationId")
        } catch (e: Exception) {
            Log.e("SpeakerRepository", "Error deleting speaker $speakerId from congregation $congregationId", e)
        }
    }

    // Wichtige Funktion zum Löschen aller Speaker einer Congregation (bevor die Congregation selbst gelöscht wird)
    override suspend fun deleteAllSpeakersFromCongregation(congregationId: String) {
        if (congregationId.isBlank()) return
        Log.d("SpeakerRepository", "Attempting to delete all speakers from congregation $congregationId")
        val speakers = getSpeakersForCongregation(congregationId)
        // Firestore bietet keine direkte Batch-Löschung für Subcollections ohne alle Dokumente einzeln zu lesen.
        // Für sehr große Subcollections sind Cloud Functions effizienter.
        // Für überschaubare Mengen ist dies clientseitig okay:
        withContext(Dispatchers.IO) { // Operationen in einem Hintergrund-Thread ausführen
            speakers.forEach { speaker ->
                try {
                    firestoreService.getSpeakersSubcollection(congregationId)
                        .document(speaker.speakerId).delete().await()
                    Log.d("SpeakerRepository", "Deleted speaker ${speaker.speakerId} from $congregationId")
                } catch (e: Exception) {
                    Log.e(
                        "SpeakerRepository",
                        "Error deleting speaker ${speaker.speakerId} during batch delete for $congregationId",
                        e
                    )
                    // Optional: Sammle Fehler und werfe eine aggregierte Exception
                }
            }
        }
        Log.d(
            "SpeakerRepository",
            "Finished deleting speakers for congregation $congregationId. Count: ${speakers.size}"
        )
    }

    /**
     * Aktualisiert den Aktivitätsstatus eines bestimmten Speakers.
     *
     * @param congregationId Die ID der Congregation, zu der der Speaker gehört.
     * @param speakerId Die ID des Speakers, der aktualisiert werden soll.
     * @param isActive Der neue Aktivitätsstatus (true für aktiv, false für inaktiv).
     * @return True, wenn das Update erfolgreich war, sonst false.
     */
    override suspend fun setSpeakerActive(congregationId: String, speakerId: String, isActive: Boolean): Boolean {
        if (congregationId.isBlank() || speakerId.isBlank()) {
            Log.w(
                "SpeakerRepository",
                "setSpeakerActive: Congregation ID or Speaker ID is blank. congregationId: $congregationId, " +
                    "speakerId: $speakerId"
            )
            return false
        }

        return try {
            val speakerDocRef = firestoreService.getSpeakersSubcollection(congregationId)
                .document(speakerId)

            // Verwende update() um nur spezifische Felder zu aktualisieren, ohne das gesamte Objekt zu überschreiben.
            // Das ist effizienter und sicherer, falls das Speaker-Objekt noch andere Felder hat,
            // die hier nicht angefasst werden sollen.
            speakerDocRef.update("isActive", isActive).await()
            Log.d("SpeakerRepository", "Speaker $speakerId in congregation $congregationId isActive set to $isActive.")
            true
        } catch (e: Exception) {
            Log.e(
                "SpeakerRepository",
                "Error updating speaker $speakerId active status in congregation" +
                    " $congregationId",
                e
            )
            // Hier könntest du spezifischere Fehlerbehandlung basierend auf der Exception e machen,
            // z.B. prüfen, ob das Dokument überhaupt existiert (obwohl update() keinen Fehler wirft, wenn das Dokument
            // nicht existiert).
            false
        }
    }
}
