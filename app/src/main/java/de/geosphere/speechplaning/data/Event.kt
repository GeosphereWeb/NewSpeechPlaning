package de.geosphere.speechplaning.data

import android.content.Context
import androidx.annotation.StringRes
import de.geosphere.speechplaning.R

/**
 * Enum class representing different types of events.
 *
 * Each event has a resource ID associated with it, which can be used to retrieve
 * the string representation of the event's name in a given context.
 *
 * @property resourceId The resource ID of the string representing the event's name.
 */
enum class Event(@StringRes val resourceId: Int) {
    CIRCUIT_ASSEMBLY_WITH_CIRCUIT_OVERSEER(R.string.CIRCUIT_ASSEMBLY_WITH_CIRCUIT_OBSERVER),
    CIRCUIT_OVERSEER_CONGREGATION_VISIT(R.string.CIRCUIT_OVERSEER_CONGREGATION_VISIT),
    CONVENTION(R.string.CONVENTION),
    MEMORIAL(R.string.MEMORIAL),
    SPECIAL_LECTURE(R.string.SPECIAL_LECTURE),
    MISCELLANEOUS(R.string.MISCELLANEOUS),
    UNKNOWN(R.string.UNKNOWN),
    ;

    fun getString(context: Context): String = context.getString(this.resourceId)
}
