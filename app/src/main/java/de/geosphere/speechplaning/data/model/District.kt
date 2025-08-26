package de.geosphere.speechplaning.data.model

import com.google.firebase.firestore.DocumentId
import kotlinx.serialization.Serializable


@Serializable
data class District(
    @DocumentId val id: String = "",
    val circuitOverseerId: String = "",
    val congregationIds: List<String> = emptyList(),
    val active: Boolean = true,
) : SavableDataClass()
