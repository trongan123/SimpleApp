pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven("https://storage.googleapis.com/download.flutter.io")
        maven("https://jitpack.io")
    }
}

rootProject.name = "SimpleApp"
include(":app")

val flutterProjectRoot = file("app_flutter")
apply(from = File(flutterProjectRoot, ".android/include_flutter.groovy"))