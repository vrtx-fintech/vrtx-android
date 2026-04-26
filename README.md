# vrtx-android

Android SDK wrapper for **vrtx Pay**. Drop one AAR into your app and your users get the full vrtx onboarding, wallet and card flows — no extra credentials, no extra repositories to wire up for the SDK source.

The wrapper is a single fused AAR: the entire vrtx SDK is bundled inside, so consumers never authenticate against the private source registry.

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

Tagged releases attach `vrtx-android-<version>.aar`, `.pom`, and `.module` to their [GitHub Release](https://github.com/vrtx-fintech/vrtx-android/releases). Point Gradle at that download URL with an Ivy repository that reads the Gradle module metadata — every transitive dep (Compose, Retrofit, biometric, etc.) resolves automatically from public Maven.

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // The wrapper's `emvnfccard` transitive is JitPack-only:
        maven { url = uri("https://jitpack.io") }
        // The wrapper itself ships from GitHub Releases:
        ivy {
            url = uri("https://github.com/vrtx-fintech/vrtx-android/releases/download")
            patternLayout {
                artifact("[revision]/vrtx-android-[revision].[ext]")
            }
            metadataSources { gradleMetadata() }
            content { includeModule("com.github.vrtx-fintech", "vrtx-android") }
        }
    }
}
```

```kotlin
// app/build.gradle.kts
dependencies {
    implementation("com.github.vrtx-fintech:vrtx-android:0.1.0")
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

Internal — vrtx fintech.
