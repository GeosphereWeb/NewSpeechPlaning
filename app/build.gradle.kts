import java.util.Locale

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "de.geosphere.speechplaning"
    compileSdk = 36

    defaultConfig {
        applicationId = "de.geosphere.speechplaning"
        minSdk = 33
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
//            signingConfig = signingConfigs.getByName("debug")
        }
        debug {
            // Replace testCoverageEnabled = true
            // enableUnitTestCoverage = true // To enable coverage for unit tests
            // enableAndroidTestCoverage = true // To enable coverage for instrumented tests
            // Or enable both if needed:
            isMinifyEnabled = false // Usually false for debug builds
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = listOf("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
    }
    buildFeatures {
        compose = true
    }

    lint {
        baseline = file("lint-baseline.xml")
    }

    packaging {
        resources {
            excludes.add("META-INF/LICENSE.md")
            excludes.add("META-INF/LICENSE-notice.md")
        }
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)

    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.gson)

    // Import the Firebase BoM
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries
    // Add the dependency for the Realtime Database library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation(libs.firebase.database)

    // // Koin
    // // dependencies with Koin
    implementation(libs.koin.android) // Oder die neueste Version
    implementation(libs.koin.androidx.compose)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidx.ui.test.junit4.android) // Für

    testImplementation(kotlin("test"))
    testImplementation(libs.junit.jupiter.api)

    testImplementation(libs.junit.jupiter.params)
    testImplementation(libs.mockk.android)
    testImplementation(libs.mockk.agent)
    testImplementation(libs.kotlinx.coroutines.test)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.mockk.agent)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    detekt(libs.detekt.cli)
    detektPlugins(libs.detekt.formatting)


    testRuntimeOnly(libs.junit.jupiter.engine)

}

// INHALT FÜR DIE DUMMY google-services.json
// WICHTIG: Dieser Inhalt muss EXAKT dem Inhalt der Datei entsprechen,
// die du in 'app/google-services.json' in Git eingecheckt hast!
val dummyJsonContent = """
{
  "project_info": {
    "project_number": "0",
    "firebase_url": "https://dummy.firebaseio.com",
    "project_id": "dummy-project",
    "storage_bucket": "dummy-project.appspot.com"
  },
  "client": [
    {
      "client_info": {
        "mobilesdk_app_id": "1:0:android:0",
        "android_client_info": {
          "package_name": "de.geosphere.speechplaning"
        }
      },
      "oauth_client": [],
      "api_key": [
        {
          "current_key": "dummy_api_key"
        }
      ],
      "services": {
        "appinvite_service": {
          "other_platform_oauth_client": []
        }
      }
    }
  ],
  "configuration_version": "1"
}
""".trimIndent()

// Pfad zur ECHTEN, lokalen google-services.json im Projekt-Root (NICHT IN GIT)
val realLocalGoogleServices = rootProject.file("google-services.json")
// Pfad zur google-services.json im app-Modul (diese ist mit DUMMY-Inhalt in GIT)
val targetAppGoogleServices = project.file("google-services.json")

// Task, um temporär die ECHTE google-services.json zu verwenden, falls lokal vorhanden
val useRealGoogleServicesTask = tasks.register("useRealGoogleServicesForLocalBuild") {
    description = "Task, um temporär die ECHTE google-services.json zu verwenden, falls lokal vorhanden"
    group = "build"
    outputs.upToDateWhen { false }
    onlyIf { realLocalGoogleServices.exists() } // Nur ausführen, wenn die ECHTE Datei im Projekt-Root existiert
    doLast {
        if (realLocalGoogleServices.exists()) {
            println(
                "LOKALER BUILD: Kopiere temporär die ECHTE google-services.json aus dem Projekt-Root in das " +
                    "app-Modul."
            )
            println("Diese wird nach dem Build durch die DUMMY-Version ersetzt.")
            realLocalGoogleServices.copyTo(targetAppGoogleServices, overwrite = true)
        }
        // Wenn die echte Datei nicht existiert (z.B. frischer Checkout ohne lokale Einrichtung, oder CI),
        // wird die bereits im app-Modul eingecheckte (Dummy-)Datei verwendet.
        // Die CI-Pipeline überschreibt diese ohnehin mit dem Inhalt aus dem Secret.
    }
}

// Task, um die DUMMY google-services.json im app-Modul wiederherzustellen
val restoreDummyGoogleServicesTask = tasks.register("restoreDummyGoogleServicesInAppModule") {
    description = "Task, um die DUMMY google-services.json im app-Modul wiederherzustellen"
    group = "build"

    // Diese Zeile sorgt dafür, dass der Task immer ausgeführt wird,
    // indem seine Outputs als niemals aktuell deklariert werden.
    outputs.upToDateWhen { false }

    doLast {
        // Stelle sicher, dass dummyJsonContent nicht leer ist (einfache Überprüfung)
        if (dummyJsonContent.isBlank()) {
            throw GradleException("Dummy JSON content is blank. Check its definition.")
        }

        // Schreibe den dummyJsonContent in die targetAppGoogleServices Datei
        targetAppGoogleServices.writeText(dummyJsonContent)
        println("INFO: Task '${name}' explicitly overwrote '${targetAppGoogleServices.absolutePath}'" +
            " with predefined dummy content.")
    }

}

// Gradle-Konfiguration, um die Tasks korrekt einzubinden
afterEvaluate {
    android.applicationVariants.forEach { variant ->
        val capitalName = variant.name.replaceFirstChar { ch ->
            if (ch.isLowerCase()) ch.titlecase(Locale.getDefault()).first() else ch
        }
        val processResourcesTaskName = "process${capitalName}GoogleServices"
        val assembleTaskName = "assemble${capitalName}" // z.B. assembleDebug

        // Stelle sicher, dass VOR der Verarbeitung der Google Services die ECHTE Datei kopiert wird (falls vorhanden)
        tasks.findByName(processResourcesTaskName)?.let {
            it.dependsOn(useRealGoogleServicesTask)
            Unit
        }

        // Stelle sicher, dass NACH dem Assemble-Task die DUMMY-Datei wiederhergestellt wird
        tasks.findByName(assembleTaskName)?.let {
            it.finalizedBy(restoreDummyGoogleServicesTask)
            Unit
        }
    }
    // Für Unit-Tests ist dies normalerweise nicht relevant, da sie google-services.json selten direkt verarbeiten.
    android.unitTestVariants.forEach { variant ->
        val capitalName = variant.name.replaceFirstChar { ch ->
            if (ch.isLowerCase()) ch.titlecase(Locale.getDefault()).first() else ch
        }
        val processResourcesTaskName = "process${capitalName}GoogleServices"
        val assembleTaskName = "test${capitalName}UnitTest" // z.B. testDebugUnitTest

        tasks.findByName(processResourcesTaskName)?.let {
            it.dependsOn(useRealGoogleServicesTask)
            Unit
        }
        tasks.findByName(assembleTaskName)?.let {
            it.finalizedBy(restoreDummyGoogleServicesTask)
            Unit
        }
    }
}
