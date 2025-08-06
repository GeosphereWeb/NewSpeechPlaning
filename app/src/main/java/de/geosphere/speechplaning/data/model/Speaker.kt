@file:Suppress("MatchingDeclarationName")

package de.geosphere.speechplaning.data.model

import com.google.firebase.firestore.DocumentId
import de.geosphere.speechplaning.data.SpiritualStatus
import kotlinx.serialization.Serializable


/**
 * Represents a speaker with their personal information and details.
 *
 * @property speakerId The unique identifier for the speaker. This is typically assigned by Firestore.
 * @property nameFirst The first name of the speaker.
 * @property nameLast The last name of the speaker.
 * @property mobile The mobile phone number of the speaker.
 * @property phone The landline phone number of the speaker.
 * @property email The email address of the speaker.
 * @property spiritualStatus The spiritual status of the speaker (e.g., Elder, Ministerial Servant).
 * @property speechNumberIds A list of IDs representing the speech numbers the speaker is qualified to give.
 * @property congregation The congregation to which the speaker belongs.
 * @property active Indicates whether the speaker is currently active and available for assignments.
 */
@Serializable
data class Speaker(
    @DocumentId val speakerId: String = "",
    val nameFirst: String = "",
    val nameLast: String = "",
    val mobile: String = "",
    val phone: String = "",
    val email: String = "",
    val spiritualStatus: SpiritualStatus = SpiritualStatus.UNKNOWN,
    val speechNumberIds: List<String> = emptyList(),
    val congregationId: String = "",
    val isActive: Boolean = true,
) : SavableDataClass()
