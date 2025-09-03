package de.geosphere.speechplaning.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import de.geosphere.speechplaning.data.model.Speaker
import kotlinx.coroutines.tasks.await

@Suppress("TooGenericExceptionCaught", "TooGenericExceptionThrown" )
class SpeakerRepository(private val firestore: FirebaseFirestore) {

    /**
     * Ruft die Referenz zur 'speakers'-Subcollection für eine bestimmte Versammlung ab.
     * @param districtId Die ID des übergeordneten Districts.
     * @param congregationId Die ID der übergeordneten Versammlung.
     */
    private fun getSpeakerCollection(districtId: String, congregationId: String) =
        firestore.collection("districts").document(districtId)
            .collection("congregations").document(congregationId)
            .collection("speakers")

    /**
     * Speichert einen Redner in der Subcollection einer bestimmten Versammlung.
     *
     * @param districtId Die ID des Districts.
     * @param congregationId Die ID der Versammlung.
     * @param speaker Das zu speichernde Speaker-Objekt.
     * @return Die ID des gespeicherten Dokuments.
     */
    suspend fun saveSpeaker(districtId: String, congregationId: String, speaker: Speaker): String {
        return try {
            val collection = getSpeakerCollection(districtId, congregationId)
            if (speaker.id.isBlank()) {
                val documentReference = collection.add(speaker).await()
                documentReference.id
            } else {
                collection.document(speaker.id).set(speaker).await()
                speaker.id
            }
        } catch (e: Exception) {
            throw RuntimeException("Failed to save speaker", e)
        }
    }

    /**
     * Ruft alle Redner für eine bestimmte Versammlung ab.
     *
     * @param districtId Die ID des Districts.
     * @param congregationId Die ID der Versammlung.
     * @return Eine Liste von Speaker-Objekten.
     */
    suspend fun getSpeakersForCongregation(districtId: String, congregationId: String): List<Speaker> {
        return try {
            getSpeakerCollection(districtId, congregationId).get().await().toObjects(Speaker::class.java)
        } catch (e: Exception) {
            throw RuntimeException("Failed to get speakers", e)
        }
    }

    // ... Ähnliche Methoden für deleteSpeaker etc.
}
