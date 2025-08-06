package de.geosphere.speechplaning.viewModels.speeches

import de.geosphere.speechplaning.data.model.Speech

sealed interface SpeechUiState {
    data class Success(val speeches: List<Speech>) : SpeechUiState
    data class Error(val message: String) : SpeechUiState
    data object Loading : SpeechUiState
    data object PermissionError : SpeechUiState
}