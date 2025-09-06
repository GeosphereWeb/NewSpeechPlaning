// Kotlin-Version des JaCoCo-Build-Skripts.

// Das jacoco-Plugin selbst und die Abhängigkeiten werden in app/build.gradle.kts verwaltet.

// Lade JaCoCo-Klassenausschlüsse aus einer externen Datei
val jacocoExclusionFile = rootProject.file("config/jacoco/jacoco_class_exclusions.txt")
val jacocoExclusionPatterns = if (jacocoExclusionFile.exists()) {
    jacocoExclusionFile.readLines().filter { it.isNotBlank() }
} else {
    println("Warning: JaCoCo class exclusion file not found at ${'$'}{jacocoExclusionFile.absolutePath}. No class exclusions will be applied.")
    emptyList<String>() // Fallback, falls die Datei nicht existiert oder leer ist
}

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
    val classDirs = fileTree("${'$'}buildDir/tmp/kotlin-classes/debug") { // <-- Pfad hier geändert
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
