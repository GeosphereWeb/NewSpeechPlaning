package de.geosphere.speechplaning.data.repository

import android.util.Log
import de.geosphere.speechplaning.data.model.Congregation
import de.geosphere.speechplaning.data.services.FirestoreService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class CongregationRepository(private val firestoreService: FirestoreService) : ICongregationRepository {
    private val collection = firestoreService.getCollection(Congregation::class.java)


    override suspend fun getCongregation(congregationId: String): Congregation? {
        if (congregationId.isBlank()) return null
        // Deine bestehende getDocument-Methode kann hier weiterhin gut verwendet werden,
        // oder du rufst direkt die spezifischere auf, falls du eine hinzufügst.
        return firestoreService.getDocument("congregations", congregationId, Congregation::class.java)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun getAllCongregation(): Flow<List<Congregation>> = callbackFlow {
        // 1. SnapshotListener registrieren
        val listenerRegistration = collection
            .addSnapshotListener { querySnapshot, error ->
                // 2. Fehlerbehandlung
                if (error != null) {
                    // Wichtig: Den Flow bei einem Fehler schließen, um Ressourcen freizugeben
                    // und den Collector zu informieren.
                    close(error) // Schließt den Flow mit einer Exception
                    return@addSnapshotListener
                }

                // 3. Datenverarbeitung
                // Wenn querySnapshot null ist (sollte bei erfolgreichem Listener nicht oft der Fall sein,
                // aber sicher ist sicher), eine leere Liste zurückgeben.
                val congregationFromFirestore = querySnapshot?.documents?.mapNotNull { documentSnapshot ->
                    // 3a. toObject mit Fehlerbehandlung pro Dokument (optional, aber robuster)
                    try {
                        documentSnapshot.toObject(Congregation::class.java)
                    } catch (e: Exception) {
                        // Hier könntest du loggen, dass ein Dokument nicht de-serialisiert werden konnte
                        // z.B. Log.e("SpeechRepository", "Error deserializing speech document
                        // ${documentSnapshot.id}", e)
                        Log.e(
                            "CongregationRepository",
                            "Error deserializing congregation document ${documentSnapshot.id}",
                            e
                        )
                        null // Ignoriere dieses Dokument, wenn es nicht passt
                    }
                } ?: emptyList() // Fallback auf leere Liste, wenn querySnapshot null ist

                // 3b. Optional: Sortierung oder Filterung der Liste
                val sortedCongregations = congregationFromFirestore.sortedBy {
                    // Annahme: Speech.number ist ein String, der in eine Zahl umgewandelt werden kann
                    // oder bereits führende Nullen für korrekte String-Sortierung hat.
                    // Wenn es bereits korrekt sortierbare Strings sind (z.B. "001", "002"),
                    // reicht it.number.
                    // Wenn es Strings wie "1", "10", "2" sind und du numerisch sortieren willst:
                    it.congregationId.toIntOrNull() ?: Int.MAX_VALUE // Fallback für fehlerhafte Nummern
                }

                // 4. Daten an den Flow senden
                // trySend blockiert nicht und ist sicher innerhalb des Callbacks zu verwenden.
                // Es ist auch gut, zu prüfen, ob der Channel noch aktiv ist, obwohl close()
                // dies handhaben sollte.
                if (!isClosedForSend) {
                    trySend(sortedCongregations).isSuccess // isSuccess kann hier ignoriert oder geloggt werden
                }
            }

        // 5. Aufräumen, wenn der Flow beendet wird (durch den Collector oder durch Fehler)
        awaitClose {
            // Wichtig: Den Listener entfernen, um Memory Leaks und unnötige Leseoperationen zu vermeiden.
            listenerRegistration.remove()
        }
    }

    override suspend fun saveCongregation(congregation: Congregation): String {
        val docRef = if (congregation.congregationId.isBlank()) {
            collection.document()
        } else {
            collection.document(congregation.congregationId)
        }
        val congregationToSave = congregation.copy(congregationId = docRef.id)

        docRef.set(congregationToSave).await() // 'set' zum Erstellen oder Überschreiben
        Log.d("CongregationRepository", "Congregation saved/updated with ID: ${docRef.id}")
        return docRef.id
    }

    override suspend fun setActive(congregationId: String, active: Boolean) {
        collection.document(congregationId).update("active", active).await()
    }
}
