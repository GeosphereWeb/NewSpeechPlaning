package de.geosphere.speechplaning.di

import com.google.firebase.firestore.FirebaseFirestore
import de.geosphere.speechplaning.data.repository.DistrictRepository
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

        // Use Cases Speech

        // viewModels

    }
