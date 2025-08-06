package de.geosphere.speechplaning.data.model

import com.google.firebase.firestore.DocumentId


/**
 * Represents a Chairman entity within the application.
 *
 * This data class is used to store and manage information about individuals
 * who can act as chairmen for events or meetings. It includes their personal
 * details and their current activity status.
 *
 * @property chairmanId The unique identifier for this Chairman, typically a Firestore document ID.
 *                      This field is annotated with `@DocumentId` to indicate its role in Firestore.
 *                      It defaults to an empty string if not provided.
 * @property nameFirst The first name of the Chairman. Defaults to an empty string.
 * @property nameLast The last name of the Chairman. Defaults to an empty string.
 * @property active A boolean flag indicating whether the Chairman is currently active.
 *                  Defaults to `true`, meaning a newly created Chairman is active by default.
 */
data class Chairman(
    // Firestore document id
    @DocumentId val chairmanId: String = "",
    val nameFirst: String = "",
    val nameLast: String = "",
    val active: Boolean = true,
) : SavableDataClass()
