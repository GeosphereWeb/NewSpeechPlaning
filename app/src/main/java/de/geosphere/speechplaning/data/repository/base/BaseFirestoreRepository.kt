package de.geosphere.speechplaning.data.repository.base

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Generische Basisimplementierung für Firestore-Repositories.
 * @param T Der Typ der Entität. Muss ein '@DocumentId'-annotiertes Feld 'id: String' haben oder
 *          die Methode 'extractIdFromEntity' muss entsprechend überschrieben werden.
 * @param firestore Die FirebaseFirestore-Instanz.
 * @param collectionPath Der Pfad zur Firestore-Collection.
 * @param clazz Die Klassenreferenz der Entität (z.B. MyEntity::class.java).
 */
@Suppress("TooGenericExceptionCaught", "TooGenericExceptionThrown")
abstract class BaseFirestoreRepository<T : Any>(
    protected val firestore: FirebaseFirestore,
    private val collectionPath: String,
    private val clazz: Class<T>
) : FirestoreRepository<T, String> { // ID ist hier als String spezialisiert

    /**
     * Extrahiert die ID aus der Entität. Muss von Subklassen überschrieben werden,
     * um das korrekte ID-Feld der Entität zurückzugeben.
     * Z.B.: return entity.id
     */
    internal abstract fun extractIdFromEntity(entity: T): String

    /**
     * Prüft, ob die ID der Entität als "leer" oder "neu" betrachtet werden soll.
     * Standardmäßig wird String.isBlank() verwendet.
     */
    protected open fun isEntityIdBlank(id: String): Boolean {
        return id.isBlank()
    }

    override suspend fun save(entity: T): String {
        val entityId = extractIdFromEntity(entity)
        return try {
            if (isEntityIdBlank(entityId)) {
                val documentReference = firestore.collection(collectionPath)
                    .add(entity)
                    .await()
                documentReference.id // Gibt die von Firestore generierte ID zurück
            } else {
                firestore.collection(collectionPath)
                    .document(entityId)
                    .set(entity)
                    .await()
                entityId // Gibt die existierende ID zurück
            }
        } catch (e: Exception) {
            val idForErrorMessage = if (isEntityIdBlank(entityId)) "[new]" else entityId
            throw RuntimeException("Failed to save entity $idForErrorMessage in $collectionPath", e)
        }
    }

    override suspend fun getById(id: String): T? {
        return try {
            if (id.isBlank()) return null // Keine gültige ID zum Abrufen
            val documentSnapshot = firestore.collection(collectionPath)
                .document(id)
                .get()
                .await()
            documentSnapshot.toObject(clazz)
        } catch (e: Exception) {
            throw RuntimeException("Failed to get entity $id from $collectionPath", e)
        }
    }

    override suspend fun getAll(): List<T> {
        return try {
            val querySnapshot = firestore.collection(collectionPath)
                .get()
                .await()
            querySnapshot.toObjects(clazz)
        } catch (e: Exception) {
            throw RuntimeException("Failed to get all entities from $collectionPath", e)
        }
    }

    override suspend fun delete(id: String) {
        try {
            require(id.isNotBlank()) { "Document ID cannot be blank for deletion." }
            firestore.collection(collectionPath)
                .document(id)
                .delete()
                .await()
        } catch (e: Exception) {
            throw RuntimeException("Failed to delete entity $id from $collectionPath", e)
        }
    }
}
