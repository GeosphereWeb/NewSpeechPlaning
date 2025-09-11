package de.geosphere.speechplaning.data.model

import com.google.firebase.firestore.DocumentId
import de.geosphere.speechplaning.data.Event
import java.util.Date


/**
 * Represents the planning for a lecture.
 *
 * This data class holds information about a specific lecture's planning,
 * including the date, the speech to speaker mapping, the related event, and the chairman.
 *
 * @property id The document ID in Firestore, typically representing the date in a specific string format.
 * @property date The date on which the lecture is planned.
 * @property event The type of event associated with this lecture planning. Defaults to [Event.UNKNOWN].
 * @property chairmanId The chairman presiding over the lecture.
 */
data class LecturePlanning(
    @DocumentId var id: Date? = null,
    val event: Event = Event.UNKNOWN,
    val chairmanId: String = "",
) : SavableDataClass()
