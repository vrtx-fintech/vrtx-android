pluginManagement {
    repositories {
        google()
        maven("https://repo1.maven.org/maven2")
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        maven("https://repo1.maven.org/maven2")
        mavenCentral()
        mavenLocal()
    }
}

rootProject.name = "vrtx-android-example"
include(":app")
