pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            credentials {
                username = "mapbox"
                password = extra["MAPBOX_ACCESS_TOKEN"]?.toString() ?: "" // Correct way to access property in Kotlin DSL
            }
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            credentials {
                username = "mapbox"
                password = extra["MAPBOX_ACCESS_TOKEN"]?.toString() ?: "" // Correct way to access property in Kotlin DSL
            }
        }
    }
}

rootProject.name = "Dorak"
include(":app")
 