import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.kotlin.dsl.libs
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

// Top-level build file where you can add configuration options common to all sub-projects/modules.

fun loadPatternsFromFile(filePath: String, descriptionForWarning: String): String {
    val exclusionFile = rootProject.file(filePath) // rootProject is available in this script's scope
    return if (exclusionFile.exists()) {
        exclusionFile.readLines().filter { it.isNotBlank() }.joinToString(",")
    } else {
        println( // Using println to match the original style
            "Warning: $descriptionForWarning file not found at ${exclusionFile.absolutePath}. " +
                "No exclusions will be applied."
        )
        "" // Fallback if the file doesn't exist or is empty
    }
}

val coverageExclusionPatterns = loadPatternsFromFile(
    "config/sonar/coverage_exclusions.txt",
    "SonarQube coverage exclusion"
)

val duplicationExclusionPatterns = loadPatternsFromFile(
    "config/sonar/duplication_exclusions.txt",
    "SonarQube duplication exclusion" // Corrected description
)

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt) apply true
    alias(libs.plugins.sonarcube) apply true
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
        property("sonar.host.url", "https://sonarcloud.io")

        property("sonar.exclusions", "**/google-services.json,another/file/to/exclude.java")
        property("sonar.sourceEncoding", "UTF-8")

        if (coverageExclusionPatterns.isNotEmpty()) {
            property("sonar.coverage.exclusions", coverageExclusionPatterns)
        }

        if (duplicationExclusionPatterns.isNotEmpty()) {
            property("sonar.cpd.exclusions", duplicationExclusionPatterns)
        }

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
