package de.geosphere.speechplaning.di

import com.google.firebase.firestore.FirebaseFirestore
import de.geosphere.speechplaning.data.repository.ICongregationRepository
import de.geosphere.speechplaning.data.repository.ISpeakerRepository
import de.geosphere.speechplaning.data.repository.ISpeechRepository
import de.geosphere.speechplaning.data.repository.CongregationRepository
import de.geosphere.speechplaning.data.repository.SpeakerRepository
import de.geosphere.speechplaning.data.repository.SpeechRepository
import de.geosphere.speechplaning.data.services.FirestoreService
import de.geosphere.speechplaning.data.services.FirestoreServiceImpl
import de.geosphere.speechplaning.domain.usecases.congregations.SaveCongregationsUseCase
import de.geosphere.speechplaning.domain.usecases.speeches.SaveSpeechUseCase
import de.geosphere.speechplaning.domain.usecases.speeches.SetSpeechActiveUseCase
import de.geosphere.speechplaning.viewModels.SpeakerDetailViewModel
import de.geosphere.speechplaning.viewModels.SpeakerViewModel
import de.geosphere.speechplaning.viewModels.congregation.CongregationViewModel
import de.geosphere.speechplaning.viewModels.speeches.SpeechViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule =
    module {
        // Database
        single<FirebaseFirestore> { FirebaseFirestore.getInstance() }
        single<FirestoreService> { FirestoreServiceImpl(get()) }

        // Repositories
        single<ISpeakerRepository> { SpeakerRepository(get()) }
        single<ISpeechRepository> { SpeechRepository(get()) }
        single<ICongregationRepository> { CongregationRepository(get()) }

        // Use Cases Speech
        factory { SaveSpeechUseCase(get()) }
        factory { SetSpeechActiveUseCase(get()) }
        factory { SaveCongregationsUseCase(get()) }

        // viewModels
        viewModel<SpeechViewModel> { SpeechViewModel(get(), get(), get()) }
        viewModel<SpeakerViewModel> { SpeakerViewModel(get(), get()) }
        viewModel<SpeakerDetailViewModel> { SpeakerDetailViewModel(get(), get(), get()) }
        viewModel { CongregationViewModel(get()) }
        // viewModel { CongregationViewModel(get(), get()) }
    }
