import java.net.URI

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        // Подключение artifactory-external.vkpartner.ru.
        maven {
            url = URI("https://artifactory-external.vkpartner.ru/artifactory/vkid-sdk-andorid/")
        }
    }
}

rootProject.name = "Repeton"
include(":app")
