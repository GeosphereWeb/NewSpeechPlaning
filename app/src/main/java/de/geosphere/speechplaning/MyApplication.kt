package de.geosphere.speechplaning

import android.app.Application
import com.google.firebase.FirebaseApp
import de.geosphere.speechplaning.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class MyApplication : Application() {
    companion object {
        const val TAG = "SpeechPlaning"
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(appModule)
        }
    }
}
