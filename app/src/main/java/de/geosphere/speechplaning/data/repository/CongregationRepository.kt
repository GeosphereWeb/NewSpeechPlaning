package de.geosphere.speechplaning.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import de.geosphere.speechplaning.data.model.Congregation
import kotlinx.coroutines.tasks.await
@Suppress("TooGenericExceptionCaught", "TooGenericExceptionThrown")
class CongregationRepository(private val firestore: FirebaseFirestore) {

    /**
     * Ruft die Referenz zur 'congregations'-Subcollection für einen bestimmten District ab.
     * @param districtId Die ID des übergeordneten Districts.
     */
    private fun getCongregationCollection(districtId: String) =
        firestore.collection("districts").document(districtId).collection("congregations")

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
            val collection = getCongregationCollection(districtId)
            if (congregation.id.isBlank()) {
                val documentReference = collection.add(congregation).await()
                documentReference.id
            } else {
                collection.document(congregation.id).set(congregation).await()
                congregation.id
            }
        } catch (e: Exception) {
            // Fange Fehler ab und logge sie oder wirf eine spezifische Exception
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
            getCongregationCollection(districtId).get().await().toObjects(Congregation::class.java)
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
            getCongregationCollection(districtId).document(congregationId).delete().await()
        } catch (e: Exception) {
            throw RuntimeException("Failed to delete congregation $congregationId", e)
        }
    }
}
