package de.geosphere.speechplaning.viewModels.congregation

import de.geosphere.speechplaning.data.model.Congregation

sealed interface CongregationUiState {
    data class Success(val congregations: List<Congregation>) : CongregationUiState
    data class Error(val message: String) : CongregationUiState
    data object Loading : CongregationUiState
    data object PermissionError : CongregationUiState
}