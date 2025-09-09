# NewSpeechPlan

## Übersicht

NewSpeechPlan ist eine Android-Anwendung (Details zur Funktionalität müssten noch ergänzt werden).
Dieses Projekt verwendet GitHub Actions für Continuous Integration und SonarQube (via SonarCloud) für die statische Code-Analyse und das Qualitätsmanagement. Die Ergebnisse der SonarQube-Analyse werden als "Code Scanning Results" in GitHub Pull Requests integriert.

Für das Projektmanagement und die Nachverfolgung von Aufgaben wird Jira eingesetzt.

## Technologie-Stack (basierend auf bisherigen Informationen)

*   **Sprache:** Kotlin
*   **Build-Tool:** Gradle
*   **CI/CD:** GitHub Actions
*   **Code-Qualität:** SonarQube/SonarCloud
*   **Testing:** JaCoCo für Code Coverage, Android Lint

## CI/CD Pipeline (`.github/workflows/SonarQube_PR_Analysis.yml`)

Der eingerichtete GitHub Actions Workflow führt bei Pull Requests auf `main`/`master` folgende Schritte aus:

1.  **Checkout Code:** Lädt den aktuellen Code-Stand des Pull Requests.
2.  **Setup Java:** Konfiguriert die Java-Umgebung (Version 17).
3.  **Build & Reports:**
    *   Erteilt Ausführungsrechte für `gradlew`.
    *   Führt `./gradlew app:lintDebug jacocoTestReport --stacktrace` aus, um Lint-Prüfungen durchzuführen und JaCoCo Code Coverage Reports zu generieren.
4.  **SonarQube Analysis:**
    *   Führt `./gradlew sonarqube --stacktrace --info` (oder ohne `--info` im Normalbetrieb) aus.
    *   Verwendet `GITHUB_TOKEN` und `SONAR_TOKEN` aus den GitHub Secrets.
    *   Konfiguriert diverse Pull-Request-spezifische Parameter für SonarQube.
    *   Ist so konfiguriert, dass ein SARIF-Report unter `app/build/reports/sonar/sonar.sarif` (oder dem in `build.gradle.kts` definierten Pfad) erstellt wird. Dies ist entscheidend für die Integration mit GitHub Code Scanning.
5.  **Upload Sonar Analysis to GitHub Code Scanning:**
    *   Verwendet die `github/codeql-action/upload-sarif@v3` Action.
    *   Lädt die generierte `sonar.sarif`-Datei hoch, um die Ergebnisse im "Security" Tab des Pull Requests anzuzeigen.

## Code-Qualität mit SonarQube

*   SonarQube wird eingesetzt, um Code Smells, Bugs, Vulnerabilities und die Code Coverage im Blick zu behalten.
*   Die Konfiguration für SonarQube (wie `sonar.projectKey`, `sonar.organization`, `sonar.host.url` und der Pfad für den SARIF-Report `sonar.sarif.output.path`) findet sich primär in den Gradle-Build-Dateien (`build.gradle.kts` auf Root-Ebene und/oder Modul-Ebene).
*   Für die lokale Analyse kann (nach entsprechender Konfiguration der SonarQube-Server-URL und ggf. des Tokens) `./gradlew sonarqube` ausgeführt werden.

## Wichtige Konfigurationspunkte für GitHub Code Scanning

*   **SARIF-Report:** Die SonarQube-Analyse muss so konfiguriert sein, dass sie einen SARIF-Report generiert. Dies geschieht durch Setzen der Eigenschaft `sonar.sarif.output.path` (z.B. in `build.gradle.kts` via `property("sonar.sarif.output.path", "...")` oder als `-D` Parameter beim Gradle-Aufruf).
*   **Upload Action:** Die `github/codeql-action/upload-sarif` Action im Workflow ist notwendig, um diesen Report zu GitHub hochzuladen.
*   **GitHub Secrets:** `SONAR_TOKEN` (und ggf. `SONAR_HOST_URL`, falls nicht in Gradle konfiguriert) müssen als Secrets im GitHub Repository hinterlegt sein, damit der Workflow auf SonarCloud zugreifen und Ergebnisse zurückmelden kann. `GITHUB_TOKEN` wird automatisch von Actions bereitgestellt und benötigt i.d.R. keine manuelle Konfiguration, muss aber die passenden Berechtigungen haben (standardmäßig meist ausreichend für Code Scanning Uploads).
*   **Branch Protection Rules:** Damit Pull Requests erst nach erfolgreicher Code-Analyse gemerged werden können, müssen in den GitHub Branch Protection Rules die entsprechenden Status-Checks (z.B. der Name des Analyse-Jobs und der Code Scanning Check) als "required" markiert werden.

## Projektmanagement

Die Planung, Verfolgung von Aufgaben und das Management des Entwicklungsprozesses für NewSpeechPlan erfolgt über Jira.

## Lokales Setup (Kurzübersicht)

1.  Klone das Repository.
2.  Öffne das Projekt in Android Studio.
3.  Stelle sicher, dass die passende JDK-Version (aktuell 17) konfiguriert ist.
4.  Für eine lokale SonarQube-Analyse müssen ggf. SonarQube-Server-Informationen in der `build.gradle.kts` oder lokalen `gradle.properties` konfiguriert werden.

---
