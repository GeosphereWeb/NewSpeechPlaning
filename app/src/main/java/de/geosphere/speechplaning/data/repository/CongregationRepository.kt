package de.geosphere.speechplaning.data.repository

import de.geosphere.speechplaning.data.model.Congregation
import de.geosphere.speechplaning.data.services.FirestoreService

@Suppress("TooGenericExceptionCaught", "TooGenericExceptionThrown")
class CongregationRepository(private val firestoreService: FirestoreService) {

    /**
     * Speichert eine Versammlung in der Subcollection eines bestimmten Districts.
     * Wenn die ID der Versammlung leer ist, wird ein neues Dokument erstellt.
     * Ansonsten wird das bestehende Dokument aktualisiert.
     *
     * @param districtId Die ID des Districts, zu dem die Versammlung gehört.
     * @param congregation Das zu speichernde Congregation-Objekt.
     * @return Die ID des gespeicherten Dokuments.
     */
    suspend fun saveCongregation(districtId: String, congregation: Congregation): String {
        return try {
            if (congregation.id.isBlank()) {
                val newId = firestoreService.addDocumentToSubcollection(
                    parentCollection = "districts",
                    parentId = districtId,
                    subcollection = "congregations",
                    data = congregation
                )
                congregation.copy(id = newId).id // Sicherstellen, dass die ID zurückgegeben wird
            } else {
                firestoreService.setDocumentInSubcollection(
                    parentCollection = "districts",
                    parentId = districtId,
                    subcollection = "congregations",
                    documentId = congregation.id,
                    data = congregation
                )
                congregation.id
            }
        } catch (e: Exception) {
            throw RuntimeException("Failed to save congregation in district $districtId", e)
        }
    }

    /**
     * Ruft alle Versammlungen für einen bestimmten District ab.
     *
     * @param districtId Die ID des Districts, dessen Versammlungen abgerufen werden sollen.
     * @return Eine Liste von Congregation-Objekten.
     */
    suspend fun getCongregationsForDistrict(districtId: String): List<Congregation> {
        return try {
            firestoreService.getDocumentsFromSubcollection(
                parentCollection = "districts",
                parentId = districtId,
                subcollection = "congregations",
                objectClass = Congregation::class.java
            )
        } catch (e: Exception) {
            throw RuntimeException("Failed to get congregations for district $districtId", e)
        }
    }

    /**
     * Löscht eine bestimmte Versammlung aus einem District.
     *
     * @param districtId Die ID des Districts.
     * @param congregationId Die ID der zu löschenden Versammlung.
     */
    suspend fun deleteCongregation(districtId: String, congregationId: String) {
        try {
            firestoreService.deleteDocumentFromSubcollection(
                parentCollection = "districts",
                parentId = districtId,
                subcollection = "congregations",
                documentId = congregationId
            )
        } catch (e: Exception) {
            throw RuntimeException(
                "Failed to delete congregation " +
                    "$congregationId", e
            )
        }
    }
}
