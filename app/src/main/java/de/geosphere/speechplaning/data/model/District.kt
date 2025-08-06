package de.geosphere.speechplaning.data.model

import kotlinx.serialization.Serializable


/**
 * Represents a district within an organization.
 *
 * A district is a geographical region that groups together a set of
 * [Congregation]s.
 *
 * @property name The name of the district. This usually corresponds to a
 *   geographical location, e.g., "North District", "West District", etc.
 *   In the database, this property serves as the document ID for the district.
 * @property congregations The list of [Congregation]s that belong to this district.
 *   This list may be empty if no congregations have been added to this
 *   district yet.
 * @property active Indicates whether this district is currently active or not.
 *   Inactive districts might be temporarily paused or decommissioned.
 */
@Serializable
data class District(
    // Firestore document id
    val name: String = "",
    val congregationIds: List<String> = emptyList(),
    val active: Boolean = true,
) : SavableDataClass()
