package de.geosphere.speechplaning.di

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.koin.android.ext.koin.androidContext
import org.koin.compose.KoinApplication

@Composable
fun PreviewKoin(content: @Composable () -> Unit) {
    val context = LocalContext.current
    KoinApplication(application = {
        androidContext(context)
        modules(appModule)
    }) {
        content()
    }
}
