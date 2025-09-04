package de.geosphere.speechplaning.data.services

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import de.geosphere.speechplaning.MyApplication.Companion.TAG
import de.geosphere.speechplaning.data.model.Chairman
import de.geosphere.speechplaning.data.model.Congregation
import de.geosphere.speechplaning.data.model.District
import de.geosphere.speechplaning.data.model.LecturePlanning
import de.geosphere.speechplaning.data.model.SavableDataClass
import de.geosphere.speechplaning.data.model.Speaker
import de.geosphere.speechplaning.data.model.Speech
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

@Suppress("TooManyFunctions", "TooGenericExceptionCaught", "TooGenericExceptionThrown")
class FirestoreServiceImpl(private val firestore: FirebaseFirestore) : FirestoreService {

    override suspend fun <T> getDocument(collection: String, documentId: String, type: Class<T>): T? {
        return try {
            val documentSnapshot = firestore.collection(collection).document(documentId).get().await()
            if (documentSnapshot.exists()) {
                documentSnapshot.toObject(type)
            } else {
                null
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: FirebaseFirestoreException) {
            Log.e(TAG, "getDocument from collection '$collection', id '$documentId' failed: ", e)
            null
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "getDocument mapping error for collection '$collection', id '$documentId': ", e)
            null
        }
    }

    override suspend fun <T> getDocuments(collection: String, type: Class<T>): List<T> {
        return try {
            val querySnapshot = firestore.collection(collection).get().await()
            querySnapshot.documents.mapNotNull { it.toObject(type) }
        } catch (e: CancellationException) {
            throw e
        } catch (e: FirebaseFirestoreException) {
            Log.e(TAG, "getDocuments from collection '$collection' failed: ", e)
            emptyList()
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "getDocuments mapping error for collection '$collection': ", e)
            emptyList()
        }
    }

    override suspend fun <T : SavableDataClass> saveDocument(collection: String, document: T): String {
        return try {
            val documentReference = firestore.collection(collection).add(document).await()
            documentReference.id
        } catch (e: CancellationException) {
            throw e
        } catch (e: FirebaseFirestoreException) {
            Log.e(TAG, "saveDocument to collection '$collection' failed: ", e)
            ""
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "saveDocument invalid data for collection '$collection': ", e)
            ""
        }
    }

    override fun <T> getCollection(clazz: Class<T>): CollectionReference {
        return when (clazz) {
            Chairman::class.java -> firestore.collection("chairmen")
            Congregation::class.java -> firestore.collection("congregations")
            District::class.java -> firestore.collection("districts")
            LecturePlanning::class.java -> firestore.collection("lecturesPlanning")
            Speaker::class.java -> {
                Log.w(
                    TAG, "Attempted to get Speaker collection as a top-level collection. " +
                        "Speakers are a subcollection of Congregations."
                )
                firestore.collection("speakers_dummy_do_not_use")
            }
            Speech::class.java -> firestore.collection("speeches")
            else -> {
                Log.e(TAG, "Unknown class type for collection: ${clazz.simpleName}")
                firestore.collection("unknown_collection_${clazz.simpleName}")
            }
        }
    }

    override fun getSpeakersSubcollection(congregationId: String): CollectionReference {
        require(congregationId.isNotBlank()) {
            "Congregation ID cannot be blank to access speakers subcollection."
        }
        return firestore.collection("congregations").document(congregationId)
            .collection("speakers")
    }

    override suspend fun <T : Any> saveDocumentWithId(
        collectionPath: String,
        documentId: String,
        document: T
    ): Boolean {
        return try {
            firestore.collection(collectionPath).document(documentId).set(document).await()
            true
        } catch (e: CancellationException) {
            throw e
        } catch (e: FirebaseFirestoreException) {
            Log.e(TAG, "saveDocumentWithId to '$collectionPath/$documentId' failed: ", e)
            false
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "saveDocumentWithId invalid data for '$collectionPath/$documentId': ", e)
            false
        }
    }

    // NEUE METHODEN FÜR SUBCOLLECTIONS
    override suspend fun <T : Any> addDocumentToSubcollection(
        parentCollection: String,
        parentId: String,
        subcollection: String,
        data: T
    ): String {
        return try {
            val documentReference = firestore.collection(parentCollection)
                .document(parentId)
                .collection(subcollection)
                .add(data)
                .await()
            documentReference.id
        } catch (e: Exception) {
            Log.e(TAG, "Error adding document to subcollection $subcollection in $parentCollection/$parentId", e)
            throw RuntimeException("Error adding document to subcollection $subcollection in " +
                "$parentCollection/$parentId", e)
        }
    }

    override suspend fun <T : Any> setDocumentInSubcollection(
        parentCollection: String,
        parentId: String,
        subcollection: String,
        documentId: String,
        data: T
    ) {
        try {
            firestore.collection(parentCollection)
                .document(parentId)
                .collection(subcollection)
                .document(documentId)
                .set(data)
                .await()
        } catch (e: Exception) {
            Log.e(TAG, "Error setting document $documentId in subcollection $subcollection in " +
                "$parentCollection/$parentId", e)
            throw RuntimeException("Error setting document $documentId in subcollection $subcollection in " +
                "$parentCollection/$parentId", e)
        }
    }

    override suspend fun <T : Any> getDocumentsFromSubcollection(
        parentCollection: String,
        parentId: String,
        subcollection: String,
        objectClass: Class<T>
    ): List<T> {
        return try {
            val querySnapshot = firestore.collection(parentCollection)
                .document(parentId)
                .collection(subcollection)
                .get()
                .await()
            querySnapshot.toObjects(objectClass)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting documents from subcollection $subcollection in $parentCollection/$parentId", e)
            throw RuntimeException("Error getting documents from subcollection $subcollection in " +
                "$parentCollection/$parentId", e)
        }
    }

    override suspend fun deleteDocumentFromSubcollection(
        parentCollection: String,
        parentId: String,
        subcollection: String,
        documentId: String
    ) {
        try {
            firestore.collection(parentCollection)
                .document(parentId)
                .collection(subcollection)
                .document(documentId)
                .delete()
                .await()
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting document $documentId from subcollection $subcollection in " +
                "$parentCollection/$parentId", e)
            throw RuntimeException("Error deleting document $documentId from subcollection $subcollection in " +
                "$parentCollection/$parentId", e)
        }
    }

    // Bestehende spezifische Methoden für Subcollections (falls vorhanden und noch genutzt)
    suspend fun <T : Any> saveSubCollectionDocumentWithId(
        parentCollectionPath: String,
        parentId: String,
        subCollectionName: String,
        documentId: String,
        document: T
    ): Boolean {
        return try {
            firestore.collection(parentCollectionPath).document(parentId)
                .collection(subCollectionName).document(documentId)
                .set(document).await()
            true
        } catch (e: CancellationException) {
            throw e
        } catch (e: FirebaseFirestoreException) {
            Log.e(
                TAG,
                "saveSubCollectionDocumentWithId to " +
                    "'$parentCollectionPath/$parentId/$subCollectionName/$documentId' failed: ",
                e
            )
            false
        } catch (e: IllegalArgumentException) {
            Log.e(
                TAG,
                "saveSubCollectionDocumentWithId invalid data for " +
                    "'$parentCollectionPath/$parentId/$subCollectionName/$documentId': ",
                e
            )
            false
        }
    }

    suspend fun <T : Any> addSubCollectionDocument(
        parentCollectionPath: String,
        parentId: String,
        subCollectionName: String,
        document: T
    ): String { // Gibt die ID des neuen Dokuments zurück
        return try {
            val docRef = firestore.collection(parentCollectionPath).document(parentId)
                .collection(subCollectionName).add(document).await()
            docRef.id
        } catch (e: CancellationException) {
            throw e
        } catch (e: FirebaseFirestoreException) {
            Log.e(TAG, "addSubCollectionDocument to '$parentCollectionPath/$parentId/$subCollectionName' failed: ", e)
            ""
        } catch (e: IllegalArgumentException) {
            Log.e(
                TAG,
                "addSubCollectionDocument invalid data for '$parentCollectionPath/$parentId/$subCollectionName': ",
                e
            )
            ""
        }
    }
}
