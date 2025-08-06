package de.geosphere.speechplaning.domain.usecases.speeches

import de.geosphere.speechplaning.data.model.Speech
import de.geosphere.speechplaning.data.repository.ISpeechRepository

class SaveSpeechUseCase(private val speechRepository: ISpeechRepository) {
    suspend operator fun invoke(speech: Speech): String = speechRepository.saveSpeech(speech)
}
