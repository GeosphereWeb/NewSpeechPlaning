package de.geosphere.speechplaning.data.model

import com.google.firebase.firestore.DocumentId
import kotlinx.serialization.Serializable

/**
 * Represents a congregation in the system.
 *
 * This data class holds information about a congregation, such as its ID (which is often the name),
 * street address, associated speakers, the district it belongs to, and its active status.
 *
 * @property congregationId The unique identifier for the congregation. This is often the name of the congregation.
 *                          It is annotated with `@DocumentId` for Firestore integration, indicating it's the document
 *                          ID.
 * @property street The street address of the congregation. This can be null if not provided.
 * @property speakers A list of [Speaker] objects associated with this congregation. Defaults to an empty list.
 * @property district The [District] to which this congregation belongs. Defaults to a new [District] object.
 * @property active A boolean flag indicating whether the congregation is currently active. Defaults to true.
 */
@Serializable
data class Congregation(
    @DocumentId val id: String = "",
    val name: String = "",
    val address: String = "",
    val meetingTime: String = "",
    val active: Boolean = true,
) : SavableDataClass()
