package de.geosphere.speechplaning.domain

/**
 * Enum class representing different filter options for speakers.
 *
 *  - `ACTIVE`: Filters for speakers that are currently active.
 *  - `INACTIVE`: Filters for speakers that are currently inactive.
 *  - `ALL`:  No filter applied, includes all speakers regardless of their active status.
 */
enum class SpeakerFilter {
    ACTIVE, INACTIVE, ALL
}
