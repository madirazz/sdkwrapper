pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://nexus.trulioo.com/repository/trulioo-public/") }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://nexus.trulioo.com/repository/trulioo-public/") }
    }
}

rootProject.name = "TruliooSDKWrapper"
include(":app")