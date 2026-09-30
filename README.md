# vrtx-android

The official Android SDK for **Vrtx** — onboarding, wallet, and card flows for your app.

## Requirements

- Android `minSdk` 29 or higher
- Android `compileSdk` 37 or higher
- Kotlin with JVM target 17 or higher
- AndroidX and Jetpack Compose

## Installation

Add the Vrtx repository and SDK dependency to your project:

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
```

```kotlin
// app/build.gradle.kts
dependencies {
    implementation("sa.vrtx.sa:vrtx-android:0.1.13")
}
```

## Configure your application

Add your application ID and certificate hash to the app module:

```kotlin
// app/build.gradle.kts
android {
    defaultConfig {
        manifestPlaceholders["vrtxPackageName"] = applicationId ?: ""
        manifestPlaceholders["vrtxCertHash"] = "YOUR_CERTIFICATE_HASH"
    }
}
```

`vrtxPackageName` must match the final application ID, including any flavor or build-type suffix.

For secure communication and data protection, configure your application manifest as follows:

```xml
<!-- app/src/main/AndroidManifest.xml -->
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    <application
        android:allowBackup="false"
        android:fullBackupContent="false"
        android:usesCleartextTraffic="false"
        android:dataExtractionRules="@xml/data_extraction_rules">
        ...
    </application>
</manifest>
```

## Generate the certificate hash

Vrtx uses the SHA-256 fingerprint of your signing certificate, encoded as Base64, to verify the application.

Run the following command with your signing keystore:

```bash
keytool -list -v -keystore path/to/your/keystore.jks -alias your_alias
```

Copy the `SHA256` fingerprint, remove the colons, and convert it to Base64:

```bash
echo -n "YOUR_SHA256_FINGERPRINT" | tr -d ':' | xxd -r -p | base64
```

Add the resulting value to `vrtxCertHash`. If your application uses separate debug and release signing certificates, provide both values as a comma-separated list.

## Start a Vrtx session

Call `Vrtx.setup` from an activity or another user-initiated UI event:

```kotlin
import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import sa.vrtx.public.Vrtx
import sa.vrtx.public.configuration.DesignOption
import sa.vrtx.public.configuration.Environment
import sa.vrtx.public.configuration.Language
import sa.vrtx.public.configuration.Mode
import sa.vrtx.public.configuration.theme.ThemeOptions
import sa.vrtx.public.configuration.theme.VrtxColors
import sa.vrtx.public.configuration.theme.VrtxRadius
import sa.vrtx.public.configuration.theme.VrtxSpacing

val customThemeOptions = ThemeOptions(
    cardImage = Uri.parse("https://example.com/card.png"),
    brandLogo = Uri.parse("https://example.com/logo.png"),
    brandName = "Your Brand",
    colors = VrtxColors(
        allBrands = VrtxColors.AllBrands(
            primary = Color(0xFF377DFF),
            buttonLabel = Color.White,
        ),
        labels = VrtxColors.Labels(
            primary = Color(0xFF12233D),
            secondary = Color(0xFF60708A),
            tertiary = Color(0xFF8B9AB2),
            quaternary = Color(0xFFB8C4D6),
        ),
        fills = VrtxColors.Fills(
            primary = Color(0xFFEAF3FF),
            secondary = Color(0xFFDCEAFF),
            tertiary = Color(0xFFC5D9F5),
            quaternary = Color(0xFFADC8EC),
            vibrant = VrtxColors.Fills.Vibrant(
                secondary = Color(0xFF5CA9FF),
            ),
        ),
        backgrounds = VrtxColors.Backgrounds(
            primary = Color(0xFFF4F8FF),
            secondary = Color(0xFFF7FAFF),
        ),
        backgroundsGradient = VrtxColors.BackgroundsGradients(
            wb01 = Color(0xFFEAF3FF),
            wb02 = Color(0xFFDCEBFF),
        ),
        accents = VrtxColors.Accents(
            red = Color(0xFFE05252),
            green = Color(0xFF2E9B67),
            greenBg = Color(0xFFE1F5EA),
        ),
    ),
    spacing = VrtxSpacing(
        sm = 8.dp,
        md = 12.dp,
        ml = 16.dp,
        lg = 20.dp,
    ),
    radius = VrtxRadius(
        s = 6.dp,
        sm = 8.dp,
        md = 12.dp,
        lg = 20.dp,
        full = 999.dp,
        huge = 64.dp,
    ),
)

Vrtx.setup(
    clientId = "YOUR_CLIENT_ID",
    clientSecret = "YOUR_CLIENT_SECRET",
    environment = Environment.Sandbox,
    language = Language.English,
    designOption = DesignOption.OptionC,
    mode = Mode.LIGHT,
    theme = customThemeOptions,
    fontFamily = FontFamily.Default,
    externalReference = "YOUR_EXTERNAL_REFERENCE",
    onSuccess = {
        // The Vrtx experience is ready.
    },
    onError = { error ->
        // Handle the error in your application.
    },
    onExit = {
        // The user has left the Vrtx experience.
    },
)
```

Keep production credentials in your secure build or secrets-management system. Do not commit them to source control.

Only one Vrtx session can be active at a time. Disable or guard the entry point while a session is launching or open.

## Configuration reference

| Parameter | Required | Description |
| --- | --- | --- |
| `clientId` | Yes | Client ID provided by Vrtx. |
| `clientSecret` | Yes | Client secret provided by Vrtx. |
| `environment` | Yes | `Sandbox`, `Staging`, or `Production`. |
| `fontFamily` | Yes | A font available in your application. |
| `language` | No | `English` or `Arabic`. Defaults to `English`. |
| `designOption` | No | `OptionA`, `OptionB`, or `OptionC`. Defaults to `OptionC`. |
| `mode` | No | `LIGHT` or `DARK`. Defaults to `LIGHT`. |
| `theme` | No | Optional branding and design-token customization. |
| `externalReference` | No | An application-defined reference for the session. |
| `onSuccess` | No | Called when the Vrtx experience is ready. |
| `onError` | No | Called when the session cannot be started or completed. |
| `onExit` | No | Called when the user leaves the Vrtx experience. |

Use `Environment.Sandbox` during integration, `Environment.Staging` for pre-release validation, and `Environment.Production` for live users.

## Branding and theming

The `ThemeOptions` example above shows the complete branding configuration. You can customize the logo, card image, brand name, colors, spacing, and corner radius.

All theme properties are optional. Unspecified properties use the Vrtx defaults. Use `Vrtx.defaultThemeOptions()` as a starting point when you need the complete default theme.

## Privacy and security

The SDK uses device, network, biometric, and application-integrity signals to help protect the Vrtx experience. NFC may be used when your integration supports card reading.

Review the permissions included in your final application and reflect applicable data processing in your privacy policy and Google Play Data Safety declaration. Your organization is responsible for ensuring that its disclosures and user notices are accurate for the features it enables.

## Support

For credentials, license keys, and integration help, contact your Vrtx account manager or [contact@vrtx.sa](mailto:contact@vrtx.sa).

## License

Licensed under the [Apache License, Version 2.0](LICENSE). Copyright © 2026 vrtx fintech.
