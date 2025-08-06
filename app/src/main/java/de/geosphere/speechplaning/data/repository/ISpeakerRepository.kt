package de.geosphere.speechplaning.data.repository

import android.util.Log
import de.geosphere.speechplaning.data.model.Speaker
import de.geosphere.speechplaning.data.model.Speech
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Interface for managing speaker data.
 *
 * This interface defines the contract for interacting with speaker data, including retrieving, saving, and updating
 * speaker information.
 */
interface ISpeakerRepository {

    // /**
    //  * Saves a [Speaker] object to Firestore.
    //  *
    //  * This function handles the saving process of a speaker entity to Firestore.
    //  * If the provided speaker does not have an ID, a new ID will be generated
    //  * by Firestore and assigned to the speaker before saving.
    //  *
    //  * @param speaker The [Speaker] object to be saved.
    //  * @return The ID of the saved speaker (either the original ID or the newly generated one).
    //  * @throws Exception if there is an issue during the save operation.
    //  */
    // suspend fun saveSpeakerWithUpdatedSpeeches(speaker: Speaker): String

    // /**
    //  * Sets the active state of a speaker in the database.
    //  *
    //  * This function updates the "active" field of a specific speaker document in the database.
    //  *
    //  * @param speakerId The unique identifier of the speaker to update.
    //  * @param active A boolean indicating whether the speaker should be active (true) or inactive (false).
    //  * @throws Exception if there is an error accessing the database or updating the document.
    //  */
    // suspend fun setSpeakerActive(speakerId: String, active: Boolean)

    // /**
    //  * Updates the speeches associated with a specific speaker.
    //  *
    //  * This function replaces the existing list of speeches for the given speaker with the new list provided.
    //  * It's designed to be used in a coroutine environment (hence the `suspend` modifier) allowing for
    //  * asynchronous operations, such as network requests or database updates, to be performed without
    //  * blocking the main thread.
    //  *
    //  * @param speaker The [Speaker] object whose speeches are to be updated.
    //  * @param speeches The new [List] of [Speech] objects to be associated with the speaker.
    //  *                 This list will replace any previously associated speeches.
    //  *
    //  * @throws Exception if there's any error during the update process (e.g., database error, network failure).
    //  *
    //  * Example Usage:
    //  * ```kotlin
    //  * val mySpeaker = Speaker(id = 1, name = "John Doe")
    //  * val newSpeeches = listOf(
    //  *     Speech(id = 1, title = "Speech 1", content = "Content 1"),
    //  *     Speech(id = 2, title = "Speech 2", content = "Content 2")
    //  * )
    //  * try {
    //  *     updateSpeakerSpeeches(mySpeaker, newSpeeches)
    //  *     println("Successfully updated speeches for ${mySpeaker.name}")
    //  * } catch (e: Exception) {
    //  *     println("Error updating speeches: ${e.message}")
    //  * }
    //  * ```
    //  */
    // suspend fun updateSpeakerSpeeches(speaker: Speaker, speeches: List<Speech>)
    fun getSpeakerFlow(congregationId: String, speakerId: String): Flow<Speaker?>

    fun getSpeakersForCongregationFlow(congregationId: String): Flow<List<Speaker>>

    suspend fun saveSpeaker(congregationId: String, speaker: Speaker): String

    suspend fun getSpeakersForCongregation(congregationId: String): List<Speaker>

     suspend fun getSpeaker(congregationId: String, speakerId: String): Speaker?

     suspend fun deleteSpeaker(congregationId: String, speakerId: String)

    // Wichtige Funktion zum Löschen aller Speaker einer Congregation (bevor die Congregation selbst gelöscht wird)
     suspend fun deleteAllSpeakersFromCongregation(congregationId: String)

    suspend fun setSpeakerActive(congregationId: String, speakerId: String, isActive: Boolean): Boolean

    fun getSpeakersForMultipleCongregationsFlow(congregationIdsFlow: Flow<List<String>>): Flow<List<Speaker>>

    fun getSpeakersForFixedMultipleCongregationsFlow(congregationIds: List<String>): Flow<List<Speaker>>
}
