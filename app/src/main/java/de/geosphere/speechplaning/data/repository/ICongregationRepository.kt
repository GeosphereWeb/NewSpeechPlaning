package de.geosphere.speechplaning.data.repository

import de.geosphere.speechplaning.data.model.Congregation
import kotlinx.coroutines.flow.Flow


/**
 * Data access interface for managing congregation-related data.
 *
 * This interface defines the operations for retrieving, saving, and updating
 * congregation data. It uses Kotlin Flows for asynchronous data streams and
 * suspend functions for asynchronous operations.
 */
interface ICongregationRepository {

    /**
     * Retrieves all congregations from the data source.
     *
     * This function provides a stream of all available congregations, allowing for reactive
     * handling of changes and updates.
     *
     * @return A [Flow] emitting a [List] of [Congregation] objects. Each emission represents
     *         the current list of all congregations in the data source.
     */
    fun getAllCongregation(): Flow<List<Congregation>>


    /**
     * Saves a congregation to persistent storage.
     *
     * This function is a suspend function, meaning it can be paused and resumed.
     * It takes a [Congregation] object as input, which contains the details of the congregation to be saved.
     * The function then attempts to save this congregation to a persistent data store.
     *
     * @param congregation The [Congregation] object to be saved.
     * @return A String representing the result of the operation. This could be a success message,
     *         an ID of the saved congregation, or an error message if the save failed.
     */
    suspend fun saveCongregation(congregation: Congregation): String


    /**
     * Sets the active state of a congregation.
     *
     * This function updates the active status of a congregation identified by [congregationId].
     *
     * @param congregationId The unique identifier of the congregation to update.
     * @param active True to mark the congregation as active, false to deactivate it.
     *
     * @throws Exception if there's an error updating the congregation's active status.
     */
    suspend fun setActive(congregationId: String, active: Boolean)

    suspend fun getCongregation(congregationId: String): Congregation?
}
