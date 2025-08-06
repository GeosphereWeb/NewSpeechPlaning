package de.geosphere.speechplaning.data.repository

import de.geosphere.speechplaning.data.model.Speech
import kotlinx.coroutines.flow.Flow

/**
 * Data access interface for managing speech-related data.
 *
 * This interface defines the operations for retrieving, saving, and updating
 * speech data. It uses Kotlin Flows for asynchronous data streams and
 * suspend functions for asynchronous operations.
 */
interface ISpeechRepository {
    /**
     * Retrieves all speeches from the data source.
     *
     * This function provides a stream of all available speeches, allowing for reactive
     * handling of changes and updates.
     *
     * @return A [Flow] emitting a [List] of [Speech] objects. Each emission represents
     *         the current list of all speeches in the data source.
     */
    fun getAllSpeeches(): Flow<List<Speech>>

    /**
     * Saves a speech to persistent storage.
     *
     * This function is a suspend function, meaning it can be paused and resumed.
     * It takes a [Speech] object as input, which contains the details of the speech to be saved.
     * The function then attempts to save this speech to a persistent data store.
     *
     * @param speech The [Speech] object to be saved.
     * @return A String representing the result of the operation. This could be a success message,
     *         an ID of the saved speech, or an error message if the save failed.
     */
    suspend fun saveSpeech(speech: Speech): String

    /**
     * Sets the active state of a speech item.
     *
     * This function updates the active status of a speech item identified by [speechId].
     *
     * @param speechId The unique identifier of the speech item to update.
     * @param active   True to mark the speech item as active, false to deactivate it.
     *
     * @throws Exception if there's an error updating the speech item's active status.
     */
    suspend fun setSpeechActive(speechId: String, active: Boolean)
}
