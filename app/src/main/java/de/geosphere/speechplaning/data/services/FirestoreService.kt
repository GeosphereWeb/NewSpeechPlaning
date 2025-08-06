@file:Suppress("MaxLineLength")
package de.geosphere.speechplaning.data.services

import com.google.firebase.firestore.CollectionReference
import de.geosphere.speechplaning.data.model.SavableDataClass

/**
 * Interface for interacting with Firestore, providing methods to retrieve,
 * save, and list documents within specified collections.
 */
interface FirestoreService {

    /**
     * Retrieves a document from a Firestore collection.
     *
     * @param collection The name of the Firestore collection.
     * @param documentId The ID of the document to retrieve.
     * @param type The class representing the type of the document data.
     * @return The document data as an object of type T, or null if the document does not exist or if an error occurred.
     *
     * This function asynchronously fetches a document from a specified Firestore collection. It attempts to convert
     * the retrieved document data into an object of the given type.
     *
     * Usage Example:
     * ```
     * val myDocument: MyDocument? = getDocument("users", "user123", MyDocument::class.java)
     * if (myDocument != null) {
     *     println("Document found: $myDocument")
     * } else {
     *     println("Document not found or error occurred.")
     * }
     * ```
     */
    suspend fun <T> getDocument(collection: String, documentId: String, type: Class<T>): T?

    /**
     * Retrieves a list of documents from a specified Firestore collection.
     *
     * This function asynchronously fetches documents from the given `collection` in Firestore and
     * deserializes them into objects of the specified `type`.
     *
     * @param collection The name of the Firestore collection to query.
     * @param type The class representing the type to which the documents should be deserialized.
     *             This class must have a corresponding Firestore-compatible structure or annotation.
     * @return A list of objects of type `T` representing the retrieved documents.
     * @throws Exception if there is an error during the Firestore query or data deserialization.
     *
     * Example:
     * ```kotlin
     * val users: List<User> = getDocuments("users", User::class.java)
     * ```
     */
    suspend fun <T> getDocuments(collection: String, type: Class<T>): List<T>

    /**
     * Saves a document to a specified Firestore collection.
     *
     * This function handles the asynchronous saving of a document to Firestore.
     *
     * @param T The type of the document to be saved. Must implement [SavableDataClass].
     * @param collection The name of the Firestore collection where the document should be saved.
     * @param document The document data to be saved. This must be an instance of [SavableDataClass].
     * @return The ID of the newly created document in Firestore.
     *
     * @throws Exception if there's an error during the save operation.
     */
    suspend fun <T : SavableDataClass> saveDocument(collection: String, document: T): String


    /**
     * Retrieves a Firestore collection reference based on the provided class type.
     *
     * This function dynamically determines the name of the Firestore collection
     * associated with the given class type `T` and returns a corresponding
     * `CollectionReference` object.
     *
     * @param clazz The class type `T` representing the type of data stored in the collection.
     * @return A `CollectionReference` object pointing to the Firestore collection associated with
     * the provided class type.
     * @throws IllegalArgumentException if the provided class type is not associated with a known
     * Firestore collection.
     *
     * Example Usage:
     * ```
     * val userCollection = getFirestoreCollection(User::class.java) // Returns a CollectionReference for a "users" collection
     * val productCollection = getFirestoreCollection(Product::class.java) // Returns a CollectionReference for a "products" collection
     * ```
     */

    fun <T> getCollection(clazz: Class<T>): CollectionReference

    // Du könntest die neuen spezifischeren Methoden auch ins Interface aufnehmen, wenn sie von außen aufgerufen werden sollen
    fun getSpeakersSubcollection(congregationId: String): CollectionReference
    suspend fun <T : Any> saveDocumentWithId(collectionPath: String, documentId: String, document: T): Boolean
}
