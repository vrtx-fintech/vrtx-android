# vrtx-android

The official Android SDK for **Vrtx** — onboarding, wallet, and card flows for your app.

## Requirements

| Tooling              | Minimum |
| -------------------- | ------- |
| Android `minSdk`     | 29      |
| Android `compileSdk` | 37      |
| Android Gradle Plugin| 9.1     |
| Kotlin               | 2.1     |
| JVM target           | 17      |

## Install

Add Maven Central, the Talsec freeRASP repository, and JitPack to your repositories, then declare the dependency.

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        google()
        maven("https://europe-west3-maven.pkg.dev/talsec-artifact-repository/freerasp")
        maven("https://jitpack.io")
        mavenCentral()
    }
}
```

```kotlin
// app/build.gradle.kts
dependencies {
    implementation("sa.vrtx.sa:vrtx-android:0.1.0")
}
```

Configure the manifest placeholders required by the SDK.

```kotlin
// app/build.gradle.kts
android {
    defaultConfig {
        manifestPlaceholders["vrtxPackageName"] = applicationId ?: ""
        manifestPlaceholders["vrtxCertHash"] = "YOUR_CERT_HASH"
    }
}
```

## Quick start

```kotlin
Vrtx.setup(
    clientId = "VRTX_CLIENT_ID",
    clientSecret = "VRTX_CLIENT_SECRET",
    environment = Environment.Sandbox,
    language = Language.English,
    mode = Mode.LIGHT,
    fontFamily = FontFamily.Default,
    externalReference = "YOUR_EXTERNAL_REFERENCE",
    onSuccess = { /* SDK UI launched */ },
    onError = { error -> /* surface to the user */ },
)
```

`Vrtx.setup` authenticates with Vrtx and then launches the SDK's own activity. It is not a suspend function — call it from anywhere; callbacks are delivered on the main thread.

## Contract

`Vrtx.setup` accepts these public configuration types:

| Parameter | Type | Values |
| --------- | ---- | ------ |
| `environment` | `Environment` | `Environment.Sandbox`, `Environment.Staging` |
| `language` | `Language` | `Language.English`, `Language.Arabic` |
| `mode` | `Mode` | `Mode.LIGHT`, `Mode.DARK` |
| `externalReference` | `String?` | Optional app-defined reference attached to the SDK session |

For appearance, pass `mode` and a Compose `fontFamily` built from a font already embedded in your app, such as Inter.

Omit `externalReference` when no external reference is needed. The example app sends a generated UUID string.

## Support

For credentials, license keys, and integration help, contact your Vrtx account manager or [support@vrtx.sa](mailto:support@vrtx.sa).

## License

Licensed under the [Apache License, Version 2.0](LICENSE). Copyright © 2026 vrtx fintech.
