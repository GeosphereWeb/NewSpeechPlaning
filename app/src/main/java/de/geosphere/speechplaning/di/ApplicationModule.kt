package de.geosphere.speechplaning.di

import com.google.firebase.firestore.FirebaseFirestore
import de.geosphere.speechplaning.data.repository.CongregationRepository
import de.geosphere.speechplaning.data.repository.DistrictRepository
import de.geosphere.speechplaning.data.repository.SpeakerRepository
import de.geosphere.speechplaning.data.services.FirestoreService
import de.geosphere.speechplaning.data.services.FirestoreServiceImpl
import org.koin.dsl.module

val appModule =
    module {
        // Database
        single<FirebaseFirestore> { FirebaseFirestore.getInstance() }
        single<FirestoreService> { FirestoreServiceImpl(get()) }

        // Repositories
        single { DistrictRepository(get()) }
        single { CongregationRepository(get()) }
        single { SpeakerRepository(get()) }

        // Use Cases Speech
        // TODO: Dies ist ein Test für JIRA mit automatische Erstellung von tickets


        // viewModels

    }
