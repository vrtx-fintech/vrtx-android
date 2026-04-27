# vrtx-android

The official Android SDK for **vrtx Pay** — onboarding, wallet, and card flows for your app.

## Requirements

|                      | Minimum                                                       |
| -------------------- | ------------------------------------------------------------- |
| Android `minSdk`     | 26                                                            |
| Android `compileSdk` | 36                                                            |
| AGP                  | 8.13                                                          |
| Kotlin               | 2.3                                                           |
| JVM                  | 21                                                            |
| Host activity        | `androidx.fragment.app.FragmentActivity` (biometric uses it)  |

## Install

Add `mavenCentral()` and JitPack to your repositories, then declare the dependency.

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

```kotlin
// app/build.gradle.kts
dependencies {
    implementation("io.github.abdel-monaam-aouini:vrtx-android:0.1.0")
}
```

That's it. Make sure your activity is a `FragmentActivity` and your `compileSdk` / `minSdk` match the requirements above.

## Quick start

```kotlin
import androidx.activity.compose.setContent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.rememberCoroutineScope
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.launch
import sa.vrtx.android.Vrtx
import sa.vrtx.sdk.SdkConfig
import sa.vrtx.sdk.domain.models.Environment

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val scope = rememberCoroutineScope()
            Button(onClick = {
                scope.launch {
                    Vrtx.init(
                        activity = this@MainActivity,
                        config = SdkConfig(
                            environment = Environment.Staging,
                            clientId = "YOUR_CLIENT_ID",
                            clientSecret = "YOUR_CLIENT_SECRET",
                        ),
                        onError = { error -> /* show toast / log */ },
                        onExit = { /* user closed the SDK */ },
                    )
                }
            }) { Text("Launch vrtx Pay") }
        }
    }
}
```

`Vrtx.init` is a **suspend** function — call it from a coroutine. It authenticates with vrtx and then launches the SDK's own activity.

## Configuration

`SdkConfig`:

| Field | Required | Default | Notes |
| --- | --- | --- | --- |
| `environment` | ✓ | — | `Environment.Sandbox` or `Environment.Staging` |
| `clientId` | ✓ | — | Provided by vrtx for your business client |
| `clientSecret` | ✓ | — | Provided by vrtx — keep it out of source control (use BuildConfig from `local.properties`, or a backend) |
| `language` | | `Language.English` | `Language.Arabic` available |
| `theme` | | `SdkTheme.LIGHT` | `LIGHT` / `DARK` |
| `cardType` | | `INDIVIDUAL_VIRTUAL_DEBIT_CARD` | Or `INDIVIDUAL_PHYSICAL_DEBIT_CARD` |

## Example app

A working consumer app lives under [`example/`](example/), branded as a fictional "Atlas Pay" partner integrating the wrapper. To run it locally:

```bash
# 1. Publish the wrapper to your local Maven cache
./gradlew publishToMavenLocal

# 2. Add your sandbox credentials to example/local.properties:
#      VRTX_CLIENT_ID=...
#      VRTX_CLIENT_SECRET=...
#      VRTX_ENVIRONMENT=staging

# 3. Build and install on a connected device
./gradlew -p example :app:installDebug
```

The example reads credentials from `local.properties` via `BuildConfig` — see [example/app/build.gradle.kts](example/app/build.gradle.kts) for the pattern you can reuse.

## License

Licensed under the [Apache License, Version 2.0](LICENSE). Copyright © 2026 vrtx fintech.
