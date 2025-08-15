package de.geosphere.speechplaning.data

import android.content.Context
import de.geosphere.speechplaning.R
import kotlinx.serialization.Serializable

/**
 * Represents the spiritual status of an individual within a congregation.
 *
 * This enum defines the different levels of responsibility and authority that a
 * member of a congregation might hold. Each status is associated with a
 * localized string resource ID that can be used to display the status in the user interface.
 *
 * @property naming The resource ID (e.g., `R.string.UNKNOWN`) representing the string resource
 *   associated with this spiritual status.
 */
@Serializable
enum class SpiritualStatus(
    val naming: Int,
) {
    UNKNOWN(R.string.UNKNOWN),
    MINISTERIAL_SERVANT(R.string.MINISTERIAL_SERVANT),
    ELDER(R.string.ELDER),
    ;

    fun getString(context: Context): String = context.getString(this.naming)
}
