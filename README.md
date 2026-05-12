# vrtx-android

The official Android SDK for **Vrtx** — onboarding, wallet, and card flows for your app.

## Requirements

| Tooling              | Minimum |
| -------------------- | ------- |
| Android `minSdk`     | 29      |
| Android `compileSdk` | 36      |
| Android Gradle Plugin| 8.13    |
| Kotlin               | 2.3     |
| JVM target           | 21      |

## Install

Add Maven Central to your repositories and declare the dependency.

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}
```

```kotlin
// app/build.gradle.kts
dependencies {
    implementation("sa.vrtx.sa:vrtx-android:0.0.10")
}
```

## Quick start

```kotlin
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontFamily
import sa.vrtx.public.Vrtx
import sa.vrtx.public.configuration.Environment

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Button(onClick = {
                Vrtx.setup(
                    clientId = BuildConfig.VRTX_CLIENT_ID,
                    clientSecret = BuildConfig.VRTX_CLIENT_SECRET,
                    environment = Environment.Sandbox,
                    fontFamily = FontFamily.Default,
                    onSuccess = { /* SDK UI launched */ },
                    onError = { error -> /* surface to the user */ },
                )
            }) { Text("Launch Vrtx Pay") }
        }
    }
}
```

`Vrtx.setup` authenticates with Vrtx and then launches the SDK's own activity. It is not a suspend function — call it from anywhere; callbacks are delivered on the main thread.

> **Security:** never ship a real `clientSecret` in your APK. Inject it from `local.properties` via `BuildConfig`, or — recommended for production — fetch it from your backend at runtime.

## Appearance

`Vrtx.setup` accepts:

- `themeMode: ThemeMode.LIGHT` or `ThemeMode.DARK` to match your app's appearance.
- `fontFamily:` — pass a Compose `FontFamily` built from a font already embedded in your app (e.g. Inter).

## Localization

Supported languages: English and Arabic. Pass `language = Language.English` or `language = Language.Arabic` when calling `Vrtx.setup`.

## Support

For credentials, license keys, and integration help, contact your Vrtx account manager or [support@vrtx.sa](mailto:support@vrtx.sa).

## License

Licensed under the [Apache License, Version 2.0](LICENSE). Copyright © 2026 vrtx fintech.
