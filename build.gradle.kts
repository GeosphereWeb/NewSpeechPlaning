import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.kotlin.dsl.libs
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

// Top-level build file where you can add configuration options common to all sub-projects/modules.

// Load coverage exclusion patterns from file
val coverageExclusionFile = rootProject.file("config/sonar/coverage_exclusions.txt")
val coverageExclusionPatterns = if (coverageExclusionFile.exists()) {
    coverageExclusionFile.readLines().filter { it.isNotBlank() }.joinToString(",")
} else {
    println("Warning: SonarQube coverage exclusion file not found at ${coverageExclusionFile.absolutePath}. No exclusions will be applied.")
    "" // Fallback, falls die Datei nicht existiert oder leer ist
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt) apply true
    id("org.sonarqube") version "6.2.0.5505"
    alias(libs.plugins.android.library) apply false // Überprüfe die neueste Version
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(libs.ktlint.gradle)
    }
}

sonarqube {
    properties {
        property("sonar.projectKey", "GeosphereWeb_NewSpeechPlan")
        property("sonar.organization", "geosphereweb")
        // sonar.host.url wird oft hier gesetzt, kann aber auch in der Action übergeben werden
        property("sonar.host.url", "https://sonarcloud.io")

        // HIER: Datei(en) von der SonarQube-Analyse ausschließen (allgemein)
        property("sonar.exclusions", "**/google-services.json,another/file/to/exclude.java")
        property("sonar.sourceEncoding", "UTF-8") // Explizit setzen

        // HIER: Quelldateien von der CODE COVERAGE ausschließen (aus Datei geladen)
        if (coverageExclusionPatterns.isNotEmpty()) {
            property("sonar.coverage.exclusions", coverageExclusionPatterns)
        }

        // Pfad zum JaCoCo XML-Report
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            "${project(":app").buildDir}/reports/jacoco/jacocoTestReport/jacocoTestReport.xml"
        )
        property("sonar.androidLint.reportPaths", "${project(":app").buildDir}/reports/lint-results.xml")
        property("sonar.gradle.skipCompile", "true")

        // Weitere Eigenschaften nach Bedarf (z.B. sonar.sources, sonar.java.binaries, etc.)
        // Diese werden oft automatisch durch das Gradle-Plugin und die Projektstruktur erkannt.
    }
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint") // Version should be inherited from parent

    ktlint {
        android.set(true)
        outputColorName.set("RED")
        ignoreFailures.set(false)
        enableExperimentalRules.set(true)
    }

    // Optionally configure plugin
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        debug.set(true)
        reporters {
            reporter(ReporterType.PLAIN)
            reporter(ReporterType.CHECKSTYLE)
        }
        kotlinScriptAdditionalPaths {
            include(fileTree("scripts/"))
        }
        filter {
            exclude("**/generated/**")
            include("**/kotlin/**")
        }
    }
}

// Kotlin DSL
tasks.withType<Detekt>().configureEach {
    reports {
        xml.required.set(true)
        html.required.set(true)
        txt.required.set(true)
        sarif.required.set(true)
        md.required.set(true)
    }
}

detekt {
    toolVersion = libs.versions.detekt.get()
    config.setFrom(file("config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
}

dependencies {
    detekt(libs.detekt.cli)
    detektPlugins(libs.detekt.formatting)
    detekt(libs.detekt.formatting)
}
