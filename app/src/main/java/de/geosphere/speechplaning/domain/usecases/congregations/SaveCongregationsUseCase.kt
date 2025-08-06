package de.geosphere.speechplaning.domain.usecases.congregations

import de.geosphere.speechplaning.data.model.Congregation
import de.geosphere.speechplaning.data.repository.ICongregationRepository

class SaveCongregationsUseCase(private val congregationRepository: ICongregationRepository) {
    suspend operator fun invoke(congregation: Congregation): String =
        congregationRepository.saveCongregation(congregation)
}
