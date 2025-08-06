package de.geosphere.speechplaning.viewModels.speeches

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestoreException
import de.geosphere.speechplaning.data.model.Speech
import de.geosphere.speechplaning.data.repository.ISpeechRepository
import de.geosphere.speechplaning.domain.usecases.speeches.SaveSpeechUseCase
import de.geosphere.speechplaning.domain.usecases.speeches.SetSpeechActiveUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SpeechViewModel(
    private val saveSpeechUseCase: SaveSpeechUseCase,
    private val setSpeechActiveUseCase: SetSpeechActiveUseCase,
    private val speechRepository: ISpeechRepository
) : ViewModel() {

    // Zentraler StateFlow für den gesamten UI-Zustand der Reden
    private val _uiState = MutableStateFlow<SpeechUiState>(SpeechUiState.Loading)
    val uiState: StateFlow<SpeechUiState> = _uiState.asStateFlow()

    // Wenn deine Basisklasse/Interface weiterhin einen speeches Flow erfordert,
    // könntest du ihn aus dem uiState ableiten:
    val speeches: StateFlow<List<Speech>> = _uiState.map { state ->
        when (state) {
            is SpeechUiState.Success -> state.speeches
            else -> emptyList() // Oder null, je nach Signatur von speeches
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList() // Oder passender Initialwert
    )

    // Optional: Fehlerbehandler für Coroutinen
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        // Dieser Handler fängt unerkannte Exceptions aus den Coroutinen der Funktionen
        Log.e("SpeechViewModel", "Coroutine Exception Handler caught: ", throwable)
        _uiState.value = SpeechUiState.Error(throwable.localizedMessage ?: "Ein Fehler ist aufgetreten")
    }

    init {
        loadSpeeches()
    }

    // Hilfsfunktion zur Interpretation von Firestore Exceptions
    private fun interpretFirestoreException(exception: Throwable, defaultMessage: String): SpeechUiState {
        return if (exception is FirebaseFirestoreException) {
            when (exception.code) {
                FirebaseFirestoreException.Code.PERMISSION_DENIED -> {
                    Log.w("SpeechViewModel", "Firestore Permission Denied: ${exception.message}")
                    SpeechUiState.PermissionError // Spezifischer Zustand für Firestore-Berechtigungsfehler
                }
                FirebaseFirestoreException.Code.UNAUTHENTICATED -> {
                    Log.w("SpeechViewModel", "Firestore Unauthenticated: ${exception.message}")
                    // Könnte auch ein PermissionError sein oder ein spezifischer "LoginRequiredError"
                    SpeechUiState.PermissionError // Oder z.B. SpeechUiState.Error("Bitte melde dich an.")
                }
                // Hier weitere spezifische Firestore Fehlercodes behandeln, falls nötig
                // FirebaseFirestoreException.Code.UNAVAILABLE -> SpeechUiState.Error("Server nicht erreichbar.
                // Bitte prüfe deine Internetverbindung.")
                else -> {
                    Log.e("SpeechViewModel", "Firestore Exception: ${exception.message}", exception)
                    SpeechUiState.Error(exception.localizedMessage ?: defaultMessage)
                }
            }
        } else {
            // Andere Arten von Exceptions
            Log.e("SpeechViewModel", "Non-Firestore Exception: ${exception.message}", exception)
            SpeechUiState.Error(exception.localizedMessage ?: defaultMessage)
        }
    }


    private fun loadSpeeches() {
        viewModelScope.launch(exceptionHandler) {
            _uiState.value = SpeechUiState.Loading // Oder spezifischer "LoadingDetail" Zustand
            speechRepository.getAllSpeeches() // Hier verwenden wir die Methode aus dem Repository.
                .catch { exception ->
                    // Dieser catch-Block fängt Fehler, die vom Flow emittiert werden
                    Log.w("SpeechViewModel", "Catching error from getAllSpeeches flow: ", exception)
                    _uiState.value = interpretFirestoreException(exception, "Ein Fehler beim Laden ist aufgetreten")
                }.collect { speeches ->
                    // Wenn die Daten erfolgreich abgerufen wurden, aktualisieren wir _speeches.
                  _uiState.value = SpeechUiState.Success(speeches)
                }
        }
    }

    fun saveSpeech(speech: Speech) {
        viewModelScope.launch {
            saveSpeechUseCase(speech)
        }
    }

    // Implementiere alle weiteren abstrakten Methoden aus AbstractSpeechViewModel und INewSpeechViewModel
    // Wenn z.B. setSpeechActive eine abstrakte Methode ist:
    fun setSpeechActive(speechId: String, active: Boolean) {
        viewModelScope.launch(exceptionHandler) {
            // Beispiel: Repository-Methode aufrufen
           setSpeechActiveUseCase(speechId, active) // Annahme: Methode im Repository
            // Optional: uiState aktualisieren, z.B. mit einem "SpeechUpdated" Zustand
        }
    }
}
