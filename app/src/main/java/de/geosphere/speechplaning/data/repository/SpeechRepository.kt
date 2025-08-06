package de.geosphere.speechplaning.data.repository

import android.util.Log
import de.geosphere.speechplaning.data.model.Speech
import de.geosphere.speechplaning.data.services.FirestoreService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class SpeechRepository(firestoreService: FirestoreService) : ISpeechRepository {
    private val collection = firestoreService.getCollection(Speech::class.java)

    @OptIn(DelicateCoroutinesApi::class)
    override fun getAllSpeeches(): Flow<List<Speech>> = callbackFlow {
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
                val speechesFromFirestore = querySnapshot?.documents?.mapNotNull { documentSnapshot ->
                    // 3a. toObject mit Fehlerbehandlung pro Dokument (optional, aber robuster)
                    try {
                        documentSnapshot.toObject(Speech::class.java)
                    } catch (e: Exception) {
                        // Hier könntest du loggen, dass ein Dokument nicht de-serialisiert werden konnte
                        // z.B. Log.e("SpeechRepository", "Error deserializing speech document
                        // ${documentSnapshot.id}", e)
                        Log.e("SpeechRepository", "Error deserializing speech document ${documentSnapshot.id}", e)
                        null // Ignoriere dieses Dokument, wenn es nicht passt
                    }
                } ?: emptyList() // Fallback auf leere Liste, wenn querySnapshot null ist

                // 3b. Optional: Sortierung oder Filterung der Liste
                val sortedSpeeches = speechesFromFirestore.sortedBy {
                    // Annahme: Speech.number ist ein String, der in eine Zahl umgewandelt werden kann
                    // oder bereits führende Nullen für korrekte String-Sortierung hat.
                    // Wenn es bereits korrekt sortierbare Strings sind (z.B. "001", "002"),
                    // reicht it.number.
                    // Wenn es Strings wie "1", "10", "2" sind und du numerisch sortieren willst:
                    it.number.toIntOrNull() ?: Int.MAX_VALUE // Fallback für fehlerhafte Nummern
                }

                // 4. Daten an den Flow senden
                // trySend blockiert nicht und ist sicher innerhalb des Callbacks zu verwenden.
                // Es ist auch gut, zu prüfen, ob der Channel noch aktiv ist, obwohl close()
                // dies handhaben sollte.
                if (!isClosedForSend) {
                    trySend(sortedSpeeches).isSuccess // isSuccess kann hier ignoriert oder geloggt werden
                }
            }

        // 5. Aufräumen, wenn der Flow beendet wird (durch den Collector oder durch Fehler)
        awaitClose {
            // Wichtig: Den Listener entfernen, um Memory Leaks und unnötige Leseoperationen zu vermeiden.
            listenerRegistration.remove()
        }
    }


    override suspend fun saveSpeech(speech: Speech): String {
        val documentId: String
        val speechToSave: Speech

        if (speech.number.isBlank()) {
            // Szenario: Neue Speech, Firestore soll eine ID generieren
            val newDocumentRef = collection.document() // Firestore generiert hier eine neue Document ID
            documentId = newDocumentRef.id
            speechToSave = speech.copy(speechId = documentId) // Aktualisiere das Speech-Objekt mit der neuen ID
        } else {
            // Szenario: Existierende Speech mit einer ID oder eine spezifische ID wurde vorgegeben
            documentId = speech.speechId
            speechToSave = speech.copy() // Keine Kopie nötig, da die ID bereits im Objekt ist (oder sein sollte)
            // Alternativ, um sicherzugehen: speech.copy() wenn speech unveränderlich sein soll.
        }

        collection.document(documentId).set(speechToSave).await()
        return documentId // Gib die verwendete ID zurück (entweder die neue oder die bestehende)
    }


    override suspend fun setSpeechActive(speechId: String, active: Boolean,) {
        collection.document(speechId).update("active", active).await()
    }
}
