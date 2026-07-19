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
        maven("https://europe-west3-maven.pkg.dev/talsec-artifact-repository/freerasp")
        maven("https://jitpack.io")
        maven("https://repo1.maven.org/maven2")
        mavenCentral()
        mavenLocal()
    }
}

rootProject.name = "vrtx-android-example"
include(":app")
