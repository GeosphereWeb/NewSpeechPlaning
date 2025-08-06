package de.geosphere.speechplaning.data

import de.geosphere.speechplaning.data.model.Speech

data class SpeechWithSelection(
    val speech: Speech = Speech(),
    var isSelected: Boolean = false // Default-Wert, falls noch keine Auswahl getroffen wurde
)