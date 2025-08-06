package de.geosphere.speechplaning.data.model

import com.google.firebase.firestore.DocumentId
import kotlinx.serialization.Serializable


/**
 * Represents a speech entity with properties for identification, sequence, subject, and activity status.
 * This class is designed to be serialized and saved, likely to a database like Firebase Firestore,
 * as indicated by the `@Serializable` and `@DocumentId` annotations and inheritance from `SaveableDataClass`.
 *
 * @property number The unique identifier for the speech. Annotated with `@DocumentId`, suggesting it's used
 *                  as the document ID in a Firestore collection. Defaults to an empty string.
 * @property subject The main topic or title of the speech. Defaults to an empty string.
 * @property active A boolean flag indicating whether the speech is currently active or not.
 *                  Defaults to `true`, meaning speeches are active by default.
 */
@Serializable
data class Speech(
    @DocumentId val speechId: String = "",
    val number: String = "",
    val subject: String = "",
    val active: Boolean = true,
) : SavableDataClass()
