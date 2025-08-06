package de.geosphere.speechplaning.viewModels

import de.geosphere.speechplaning.data.model.Speaker

sealed class SpeakerDetailUIState {
    object Loading : SpeakerDetailUIState()
    data class Success(
        val speaker: Speaker, // Der aktuell angezeigte (ggf. bearbeitete) Sprecher
        val originalSpeaker: Speaker, // Der ursprüngliche Sprecher vor Bearbeitung (für Diff)
        val isNewSpeaker: Boolean,
        val isInEditMode: Boolean
    ) : SpeakerDetailUIState()

    data class Error(val message: String) : SpeakerDetailUIState()
}