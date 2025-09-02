
// Kotlin-Version des JaCoCo-Build-Skripts.

// Das jacoco-Plugin selbst und die Abhängigkeiten werden in app/build.gradle.kts verwaltet.

// Erstellt den Task, den wir im Workflow aufrufen werden.
tasks.register<JacocoReport>("jacocoTestReport") {
    // Dieser Task hängt von den normalen Unit-Tests ab.
    dependsOn("testDebugUnitTest")

    // Definiert, welche Dateitypen im Report einfließen sollen.
    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    // Pfad zu den kompilierten Klassendateien.
    // Wir schließen alles aus, was von Dagger, Hilt, DataBinding und bestimmten Android-Klassen generiert wird,
    // da wir diese nicht in unserer Code-Coverage sehen wollen.
    val classDirs = fileTree("$buildDir/intermediates/classes/debug") {
        exclude(
            "**/R.class",
            "**/R$*.class",
            "**/BuildConfig.*",
            "**/Manifest*.*",
            "**/*Test*.*",
            "android/**/*.*",
            "**/*_MembersInjector.class",
            "**/Dagger*Component.class",
            "**/Dagger*Component\$Builder.class",
            "**/Dagger*Module.class",
            "**/*_Factory.class",
            "**/*_Provide*Factory.class",
            "**/*_ViewBinding.class"
        )
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
