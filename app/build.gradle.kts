import java.util.Locale
import org.gradle.testing.jacoco.tasks.JacocoReport

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlin.serialization)
    id("jacoco")
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
            isMinifyEnabled = false // Usually false for debug builds
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
        xmlReport = true
        xmlOutput = file("build/reports/lint-results.xml")
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
    // When using the BoM, you don\'t specify versions in Firebase library dependencies
    implementation(libs.firebase.database)

    // // Koin
    // // dependencies with Koin
    implementation(libs.koin.android) // Oder die neueste Version
    implementation(libs.koin.androidx.compose)
    implementation(libs.org.jacoco.core)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidx.ui.test.junit4.android) // Für

    testImplementation(kotlin("test"))
    testImplementation(libs.junit.jupiter.api)

    testImplementation(libs.mockk)
    testImplementation(libs.mockk.android)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter.params)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.kotlinx.coroutines.test)

    testRuntimeOnly(libs.junit.jupiter.engine)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.mockk.agent.jvm)

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

// NEUER, KORREKTER BLOCK mit der androidComponents API
androidComponents {
    onVariants { variant ->
        // Macht aus "debug" -> "Debug", "release" -> "Release"
        val capitalName = variant.name.replaceFirstChar { it.titlecase(Locale.getDefault()) }

        // --- Application Tasks ---
        // z.B. processDebugGoogleServices
        tasks.findByName("process${capitalName}GoogleServices")?.dependsOn(useRealGoogleServicesTask)

        // z.B. assembleDebug
        tasks.findByName("assemble${capitalName}")?.finalizedBy(restoreDummyGoogleServicesTask)


        // --- Unit Test Tasks ---
        variant.unitTest?.let { unitTest ->
            // Macht aus "debugUnitTest" -> "DebugUnitTest"
            val unitTestCapitalName = unitTest.name.replaceFirstChar { it.titlecase(Locale.getDefault()) }

            // z.B. testDebugUnitTest
            tasks.findByName("test${unitTestCapitalName}")?.let { testTask ->
                testTask.finalizedBy(restoreDummyGoogleServicesTask)
                testTask.dependsOn(useRealGoogleServicesTask)
            }
        }

        // --- Android Test Tasks ---
        variant.androidTest?.let { androidTest ->
            // Macht aus "debugAndroidTest" -> "DebugAndroidTest"
            val androidTestCapitalName = androidTest.name.replaceFirstChar { it.titlecase(Locale.getDefault()) }

            // z.B. processDebugAndroidTestGoogleServices
            tasks.findByName("process${androidTestCapitalName}GoogleServices")?.dependsOn(useRealGoogleServicesTask)

            // z.B. assembleDebugAndroidTest
            tasks.findByName("assemble${androidTestCapitalName}")?.finalizedBy(restoreDummyGoogleServicesTask)
        }
    }
}

// Ab hier wird der Inhalt von jacoco.gradle.kts eingefügt
// Kotlin-Version des JaCoCo-Build-Skripts.

// Das jacoco-Plugin selbst und die Abhängigkeiten werden in app/build.gradle.kts verwaltet.

// Lade JaCoCo-Klassenausschlüsse aus einer externen Datei
val jacocoExclusionFile = rootProject.file("config/jacoco/jacoco_class_exclusions.txt")
val jacocoExclusionPatterns = if (jacocoExclusionFile.exists()) {
    jacocoExclusionFile.readLines().filter { it.isNotBlank() }
} else {
    println("Warning: JaCoCo class exclusion file not found at ${jacocoExclusionFile.absolutePath}. No class exclusions will be applied.")
    emptyList<String>() // Fallback, falls die Datei nicht existiert oder leer ist
}

// Erstellt den Task, den wir im Workflow aufrufen werden.
tasks.register<JacocoReport>("jacocoTestReport") {
    // Dieser Task hängt von den normalen Unit-Tests ab.
    description = "Generate Jacoco coverage reports after running tests."
    group = JavaBasePlugin.BUILD_TASK_NAME
    dependsOn("testDebugUnitTest")

    // Definiert, welche Dateitypen im Report einfließen sollen.
    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    // Pfad zu den kompilierten Klassendateien.
    // Wir schließen alles aus, was von Dagger, Hilt, DataBinding und bestimmten Android-Klassen generiert wird,
    // da wir diese nicht in unserer Code-Coverage sehen wollen.
    val classDirs = fileTree("$buildDir/tmp/kotlin-classes/debug") { // <-- Korrigiert
        exclude(jacocoExclusionPatterns) // Lade Ausschlüsse aus der Liste
    }

    // Pfad zu den Quellcode-Dateien.
    val sourceDirs = files("src/main/java", "src/main/kotlin")

    // Setzt die Quell- und Klassendateien für den Report.
    sourceDirectories.setFrom(sourceDirs)
    classDirectories.setFrom(classDirs)

    // Wo die .exec-Dateien mit den Coverage-Daten liegen.
    executionData.setFrom(
        fileTree(buildDir) {
            include(
                "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
                "jacoco/testDebugUnitTest.exec" // Fallback-Pfad
            )
        }
    )
}
