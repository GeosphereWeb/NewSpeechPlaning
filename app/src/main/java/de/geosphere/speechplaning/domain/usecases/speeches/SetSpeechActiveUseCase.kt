package de.geosphere.speechplaning.domain.usecases.speeches

import de.geosphere.speechplaning.data.repository.ISpeechRepository

class SetSpeechActiveUseCase(private val repository: ISpeechRepository) {
    suspend operator fun invoke(speechId: String, active: Boolean) = repository.setSpeechActive(speechId, active)
}
