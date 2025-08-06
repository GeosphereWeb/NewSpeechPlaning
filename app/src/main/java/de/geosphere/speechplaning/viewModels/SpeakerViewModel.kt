package de.geosphere.speechplaning.viewModels

import androidx.lifecycle.ViewModel
import de.geosphere.speechplaning.data.model.Speaker
import de.geosphere.speechplaning.data.repository.ISpeakerRepository
import de.geosphere.speechplaning.domain.SpeakerFilter
import de.geosphere.speechplaning.viewModels.speeches.SpeechViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

open class SpeakerViewModel(
    private val speakerRepository: ISpeakerRepository,
    private val vmSpeeches: SpeechViewModel,
) : ViewModel() {
    private val _speakers = MutableStateFlow<List<Speaker>>(emptyList())
    val speakers: StateFlow<List<Speaker>> = _speakers.asStateFlow()

    private val _currentFilter = MutableStateFlow(SpeakerFilter.ACTIVE)
    val currentFilter: StateFlow<SpeakerFilter> = _currentFilter.asStateFlow()

    // Annahme: Du hast einen Flow, der die relevanten Congregation-IDs bereitstellt
    // z.B. aus Benutzereinstellungen oder einer anderen Datenquelle.
    private val _selectedCongregationIds = MutableStateFlow<List<String>>(emptyList())
    val selectedCongregationIdsFlow: StateFlow<List<String>> = _selectedCongregationIds.asStateFlow()


}
