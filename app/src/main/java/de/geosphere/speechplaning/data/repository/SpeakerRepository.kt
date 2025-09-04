package de.geosphere.speechplaning.data.repository

import de.geosphere.speechplaning.data.model.Speaker
import de.geosphere.speechplaning.data.services.FirestoreService // Import geändert

@Suppress("TooGenericExceptionCaught", "TooGenericExceptionThrown")
class SpeakerRepository(private val firestoreService: FirestoreService) { // Konstruktor geändert

    private fun parentCongregationsPath(districtId: String) = "districts/$districtId/congregations"

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
            val parentPath = parentCongregationsPath(districtId)
            if (speaker.id.isBlank()) {
                val newId = firestoreService.addDocumentToSubcollection(
                    parentCollection = parentPath, // z.B. districts/districtXYZ
                    parentId = congregationId,    // ID der Congregation
                    subcollection = "speakers",
                    data = speaker
                )
                speaker.copy(id = newId).id
            } else {
                firestoreService.setDocumentInSubcollection(
                    parentCollection = parentPath,
                    parentId = congregationId,
                    subcollection = "speakers",
                    documentId = speaker.id,
                    data = speaker
                )
                speaker.id
            }
        } catch (e: Exception) {
            // Es ist besser, spezifischere Fehler-Wrapper zu verwenden, wenn möglich
            throw RuntimeException("Failed to save speaker for congregation $congregationId", e)
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
            val parentPath = parentCongregationsPath(districtId)
            firestoreService.getDocumentsFromSubcollection(
                parentCollection = parentPath,
                parentId = congregationId,
                subcollection = "speakers",
                objectClass = Speaker::class.java
            )
        } catch (e: Exception) {
            throw RuntimeException("Failed to get speakers for congregation $congregationId", e)
        }
    }

    /**
     * Löscht einen bestimmten Redner.
     *
     *
     * @param districtId Die ID des Districts.
     * @param congregationId Die ID der Versammlung, zu der der Redner gehört.
     * @param speakerId Die ID des zu löschenden Redners.
     */
    suspend fun deleteSpeaker(districtId: String, congregationId: String, speakerId: String) {
        try {
            val parentPath = parentCongregationsPath(districtId)
            firestoreService.deleteDocumentFromSubcollection(
                parentCollection = parentPath,
                parentId = congregationId,
                subcollection = "speakers",
                documentId = speakerId
            )
        } catch (e: Exception) {
            throw RuntimeException("Failed to delete speaker $speakerId", e)
        }
    }
}
