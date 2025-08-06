package de.geosphere.speechplaning.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.geosphere.speechplaning.data.model.Speaker
import de.geosphere.speechplaning.data.model.Speech // Annahme: Wird für die Reden des Sprechers benötigt
import de.geosphere.speechplaning.data.repository.ISpeakerRepository
import de.geosphere.speechplaning.viewModels.speeches.SpeechViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

open class SpeakerDetailViewModel(
    private val speakerRepository: ISpeakerRepository,
    private val vmSpeeches: SpeechViewModel, // Falls benötigt, um Reden auszuwählen/anzuzeigen
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val speakerId: String? = savedStateHandle["speakerId"] // "speakerId" ist der Navigationsargument-Name

    // Interner Zustand für den Speaker, der bearbeitet wird
    private val _editableSpeaker = MutableStateFlow<Speaker?>(null)
    // Kombinierter UI-Zustand für die Detailansicht
    private val _uiState = MutableStateFlow<SpeakerDetailUIState>(SpeakerDetailUIState.Loading)
    val uiState: StateFlow<SpeakerDetailUIState> = _uiState.asStateFlow()

    init {
        val receivedSpeakerId: String? = savedStateHandle["speakerId"] // Der Schlüssel ist der Property-Name "speakerId"
        // ... Logik zum Laden des Speakers basierend auf receivedSpeakerId ...
        loadSpeakerData()
    }
    
    // Flow, der angibt, ob ungespeicherte Änderungen vorhanden sind
    val hasUnsavedChanges: StateFlow<Boolean> = uiState.map { state ->
        if (state is SpeakerDetailUIState.Success) {
            state.speaker != state.originalSpeaker && state.isInEditMode
        } else {
            false
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    // Flow für die Reden, die dem aktuellen (angezeigten/bearbeiteten) Sprecher zugeordnet sind
    val currentSpeakerSpeeches: StateFlow<List<Speech>> = combine(
        uiState,
        vmSpeeches.speeches // Alle verfügbaren Reden
    ) { detailState, allSpeeches ->
        if (detailState is SpeakerDetailUIState.Success) {
            val currentSpeakerSpeechNumbers = detailState.speaker.speechNumberIds
            allSpeeches.filter { speech ->
                currentSpeakerSpeechNumbers.contains(speech.number.toString())
            }
        } else {
            emptyList()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private fun loadSpeakerData() {
        viewModelScope.launch {
            _uiState.value = SpeakerDetailUIState.Loading
            if (speakerId == null || speakerId == "new_speaker_id") { // Annahme: "new_speaker_id" für neue Speaker
                val newSpeaker = Speaker()
                _editableSpeaker.value = newSpeaker
                _uiState.value = SpeakerDetailUIState.Success(
                    speaker = newSpeaker,
                    originalSpeaker = newSpeaker, // Kein Unterschied am Anfang
                    isNewSpeaker = true,
                    isInEditMode = true // Neue Speaker starten im Edit-Modus
                )
            } else {
                try {
                    // Annahme: getSpeakerById gibt Flow<Speaker?> zurück. Passe an, falls suspend fun.
                    val speakerFromRepo = speakerRepository.getSpeakerById(speakerId).firstOrNull()
                    if (speakerFromRepo != null) {
                        _editableSpeaker.value = speakerFromRepo.copy() // Kopie für potenzielle Bearbeitung
                        _uiState.value = SpeakerDetailUIState.Success(
                            speaker = speakerFromRepo.copy(), // separate Kopie für _editableSpeaker
                            originalSpeaker = speakerFromRepo,
                            isNewSpeaker = false,
                            isInEditMode = false // Bestehende Speaker starten im Ansichtsmodus
                        )
                    } else {
                        _uiState.value = SpeakerDetailUIState.Error("Sprecher mit ID $speakerId nicht gefunden.")
                        // Optional: Hier auch einen neuen Speaker initialisieren, wenn das gewünscht ist
                    }
                } catch (e: Exception) {
                    _uiState.value = SpeakerDetailUIState.Error("Fehler beim Laden des Sprechers: ${e.message}")
                }
            }
        }
    }

    fun enterEditMode() {
        val currentState = _uiState.value
        if (currentState is SpeakerDetailUIState.Success) {
            _uiState.value = currentState.copy(isInEditMode = true)
        }
    }

    fun exitEditModeAndDiscardChanges() {
        val currentState = _uiState.value
        if (currentState is SpeakerDetailUIState.Success) {
            // Setze den bearbeitbaren Speaker auf den originalen Zustand zurück
            _uiState.value = currentState.copy(
                speaker = currentState.originalSpeaker.copy(), // Zurücksetzen auf Original
                isInEditMode = false
            )
        }
    }

    fun updateSpeakerDetails(updatedSpeaker: Speaker) {
        val currentState = _uiState.value
        if (currentState is SpeakerDetailUIState.Success && currentState.isInEditMode) {
            _uiState.value = currentState.copy(speaker = updatedSpeaker)
        }
    }

    // Wird aufgerufen, wenn im Edit-Modus die Auswahl der Reden geändert wird
    fun updateSelectedSpeechesForSpeaker(selectedSpeeches: List<Speech>) {
        val currentState = _uiState.value
        if (currentState is SpeakerDetailUIState.Success && currentState.isInEditMode) {
            val updatedSpeaker = currentState.speaker.copy(
                speechNumberIds = selectedSpeeches.map { it.number.toString() }
            )
            _uiState.value = currentState.copy(speaker = updatedSpeaker)
        }
    }

    fun saveSpeaker() {
        val currentState = _uiState.value
        if (currentState is SpeakerDetailUIState.Success && currentState.isInEditMode) {
            viewModelScope.launch {
                try {
                    val speakerToSave = currentState.speaker
                    speakerRepository.saveSpeakerWithUpdatedSpeeches(speakerToSave) // Oder nur saveSpeaker
                    // Nach dem Speichern: originalSpeaker wird zum aktuellen Speaker
                    _uiState.value = currentState.copy(
                        originalSpeaker = speakerToSave.copy(),
                        isInEditMode = false // Verlasse den Edit-Modus
                    )
                    // Hier könntest du auch ein Event für die Navigation zurück senden
                } catch (e: Exception) {
                    // Fehler beim Speichern anzeigen, z.B. in einem temporären Error-State oder Event
                    _uiState.value = SpeakerDetailUIState.Error("Fehler beim Speichern: ${e.message}")
                    // Optional: Lasse den Nutzer im Edit-Modus, um es erneut zu versuchen
                }
            }
        }
    }
}