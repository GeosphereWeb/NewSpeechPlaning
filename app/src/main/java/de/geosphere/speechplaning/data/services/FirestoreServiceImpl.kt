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

    // Wenn du Speaker und Congregation direkt mit IDs speicherst und nicht über 'add' neue generieren lässt,
    // könntest du eine spezifischere save-Methode oder eine update-Methode benötigen,
    // oder die saveDocument so anpassen, dass sie auch mit vorgegebener ID umgehen kann (set statt add).
    // Für dieses Beispiel belassen wir sie erstmal so, da die Repositories die ID-Handhabung übernehmen.
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

    // Diese Funktion ist gut für Top-Level-Collections.
    // Für Subcollections brauchst du einen anderen Mechanismus oder musst den Pfad direkt übergeben.
    override fun <T> getCollection(clazz: Class<T>): CollectionReference {
        return when (clazz) {
            Chairman::class.java -> firestore.collection("chairmen")
            Congregation::class.java -> firestore.collection("congregations") // Beibehaltung für Top-Level
            District::class.java -> firestore.collection("districts")
            LecturePlanning::class.java -> firestore.collection("lecturesPlanning")
            Speaker::class.java -> {
                // Speaker ist jetzt eine Subcollection, daher ist ein direkter Aufruf hier nicht sinnvoll,
                // es sei denn, du hättest auch eine Top-Level "allSpeakers" Collection (was hier nicht der Fall ist).
                // Wir könnten hier eine Exception werfen oder eine leere Collection zurückgeben,
                // da getSpeakersSubcollection der richtige Weg ist.
                Log.w(
                    TAG, "Attempted to get Speaker collection as a top-level collection. " +
                        "Speakers are a subcollection of Congregations."
                )
                firestore.collection("speakers_dummy_do_not_use") // Oder Fehler werfen
            }

            Speech::class.java -> firestore.collection("speeches")
            else -> {
                Log.e(TAG, "Unknown class type for collection: ${clazz.simpleName}")
                // Zur Sicherheit, aber sollte nicht passieren
                firestore.collection("unknown_collection_${clazz.simpleName}")
            }
        }
    }

    // --- NEUE FUNKTION HINZUFÜGEN ---
    /**
     * Ruft die Referenz zur 'speakers'-Subcollection für eine gegebene Congregation-ID ab.
     */
    override fun getSpeakersSubcollection(congregationId: String): CollectionReference {
        require(congregationId.isNotBlank()) {
            "Congregation ID cannot be blank to access speakers subcollection."
        }
        // Wir verwenden die Konstante "congregations" hier direkt, da sie bekannt ist.
        // Alternativ könntest du getCollection(Congregation::class.java).document(congregationId)... verwenden
        return firestore.collection("congregations").document(congregationId)
            .collection("speakers")
    }

    // --- ERWEITERUNG FÜR SPEZIFISCHE DOKUMENTE/COLLECTIONS (optional, aber kann nützlich sein) ---
    fun getCongregationsCollection(): CollectionReference {
        return firestore.collection("congregations")
    }

    // Du könntest auch spezifische get/save Methoden für Speaker und Congregation hier anbieten,
    // wenn du die Logik nicht komplett in den Repositories haben möchtest.
    // Beispiel für ein spezifisches Speichern, das 'set' verwendet, um eine ID vorzugeben:
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
