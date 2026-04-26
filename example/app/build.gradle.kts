import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) file.inputStream().use { load(it) }
}

fun localProperty(key: String, default: String = ""): String =
    (localProperties.getProperty(key) ?: System.getenv(key) ?: default)

android {
    namespace = "sa.vrtx.example"
    compileSdk = 36

    defaultConfig {
        applicationId = "sa.vrtx.example"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "VRTX_CLIENT_ID", "\"${localProperty("VRTX_CLIENT_ID")}\"")
        buildConfigField("String", "VRTX_CLIENT_SECRET", "\"${localProperty("VRTX_CLIENT_SECRET")}\"")
        buildConfigField("String", "VRTX_ENVIRONMENT", "\"${localProperty("VRTX_ENVIRONMENT", "Sandbox")}\"")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
    }
}

dependencies {
    // Pulls the fused wrapper from JitPack (or mavenLocal during dev).
    implementation("com.github.vrtx-fintech:vrtx-android:0.1.0")

    implementation(platform("androidx.compose:compose-bom:2025.12.01"))
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.activity:activity-compose:1.12.2")
    implementation("androidx.fragment:fragment-ktx:1.8.6")
}
