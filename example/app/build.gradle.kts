import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
}

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) file.inputStream().use { load(it) }
}

fun localProperty(key: String, default: String = ""): String =
    (localProperties.getProperty(key) ?: System.getenv(key) ?: default)

val sdkVersion: String =
    (project.findProperty("sdkVersion") as String?) ?: "0.1.6"

val VRTX_CERT_HASH: String = localProperty("VRTX_CERT_HASH")
val releaseStoreFile: String = localProperty("ANDROID_KEYSTORE_FILE")
val releaseStorePassword: String = localProperty("ANDROID_KEYSTORE_PASSWORD")
val releaseKeyAlias: String = localProperty("ANDROID_KEY_ALIAS")
val releaseKeyPassword: String = localProperty("ANDROID_KEY_PASSWORD")
val hasReleaseSigningCredentials = listOf(
    releaseStoreFile,
    releaseStorePassword,
    releaseKeyAlias,
    releaseKeyPassword,
).all(String::isNotBlank)

android {
    namespace = "sa.vrtx.example"
    compileSdk = 37

    defaultConfig {
        applicationId = "sa.vrtx.example"
        minSdk = 29
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "VRTX_CLIENT_ID", "\"${localProperty("VRTX_CLIENT_ID")}\"")
        buildConfigField("String", "VRTX_CLIENT_SECRET", "\"${localProperty("VRTX_CLIENT_SECRET")}\"")
        buildConfigField("String", "VRTX_ENVIRONMENT", "\"${localProperty("VRTX_ENVIRONMENT", "Sandbox")}\"")
        manifestPlaceholders["vrtxPackageName"] = applicationId ?: ""
        manifestPlaceholders["vrtxCertHash"] = VRTX_CERT_HASH
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
        }
    }

    signingConfigs {
        getByName("release") {
            if (hasReleaseSigningCredentials) {
                storeFile = file(releaseStoreFile)
                storePassword = releaseStorePassword
                keyAlias = releaseKeyAlias
                keyPassword = releaseKeyPassword
            } else {
                initWith(getByName("debug"))
            }
        }
    }

    flavorDimensions += "environment"
    productFlavors {
        create("sandbox") {
            dimension = "environment"
            applicationIdSuffix = ".public.sandbox"
        }
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
    // Pulls the SDK from Maven Central. Override at build time with -PsdkVersion=<tag>.
    implementation("sa.vrtx.sa:vrtx-android:$sdkVersion")

    implementation(platform("androidx.compose:compose-bom:2026.06.01"))
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.activity:activity-compose:1.13.0")
    implementation("androidx.fragment:fragment-ktx:1.8.9")
}
