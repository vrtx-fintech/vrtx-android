# vrtx-android

The official Android SDK for **Vrtx** — onboarding, wallet, and card flows for your app.

## Requirements

| Tooling              | Minimum |
| -------------------- | ------- |
| Android `minSdk`     | 29      |
| Android `compileSdk` | 37      |
| Android Gradle Plugin| 9.1     |
| Kotlin               | 2.4.10    |
| JVM target           | 17      |

## 1. Add the SDK

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
    implementation("sa.vrtx.sa:vrtx-android:0.1.7")
}
```

The SDK requires `compileSdk` 37 or higher. Configure the required manifest
placeholders in your app module, using your final application ID and the
Base64-encoded SHA-256 certificate hash described below.

```kotlin
// app/build.gradle.kts
android {
    defaultConfig {
        manifestPlaceholders["vrtxPackageName"] = applicationId ?: ""
        manifestPlaceholders["vrtxCertHash"] = "YOUR_CERT_HASH"
    }
}
```

## 2. Align manifest security settings

The SDK enforces strict security defaults: backups and cleartext HTTP traffic
are disabled. If your app currently enables either, the manifest merger will
report a conflict.

For example:

```
Attribute application@allowBackup value=(true) from AndroidManifest.xml
is also present at [sa.vrtx.sa:vrtx-android:0.1.7] AndroidManifest.xml value=(false).

Attribute application@fullBackupContent value=(@xml/backup_rules) from AndroidManifest.xml
is also present at [sa.vrtx.sa:vrtx-android:0.1.7] AndroidManifest.xml value=(false).

Attribute application@usesCleartextTraffic value=(true) from AndroidManifest.xml
is also present at [sa.vrtx.sa:vrtx-android:0.1.7] AndroidManifest.xml value=(false).
```

Update the application attributes to match the SDK requirements. Do not override
these values with `tools:replace`.

```xml
<!-- app/src/main/AndroidManifest.xml -->
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <application
        android:allowBackup="false"
        android:fullBackupContent="false"
        android:usesCleartextTraffic="false"
        android:dataExtractionRules="@xml/data_extraction_rules"
        tools:targetApi="31">
        ...
    </application>
</manifest>
```

## 3. Generate your certificate hash

The SDK uses the certificate hash to verify app integrity and prevent repackaging. freeRASP requires the **SHA-256** hash of your signing certificate, converted to **Base64** format.

### Get the SHA-256 fingerprint

Open your terminal and run the following `keytool` command:

```bash
keytool -list -v -keystore path/to/your/keystore.jks -alias your_alias
```

*(For the standard debug keystore, the path is `~/.android/debug.keystore`, the alias is `androiddebugkey`, and the password is `android`)*.

Enter your keystore password when prompted. Look for the `SHA256:` fingerprint in the output. It will look like this:

```text
SHA256: 4D:5E:6F:7A:8B:9C:0D:1E:2F:3A:4B:5C:6D:7E:8F:9A:0B:1C:2D:3E:4F:5A:6B:7C:8D:9E:0F:1A:2B:3C:4D:5E
```

### Convert the hex string to Base64

Run this command (replace the hex string with your own from the previous step):

```bash
echo -n "4D:5E:6F:7A:8B:9C:0D:1E:2F:3A:4B:5C:6D:7E:8F:9A:0B:1C:2D:3E:4F:5A:6B:7C:8D:9E:0F:1A:2B:3C:4D:5E" | tr -d ':' | xxd -r -p | base64
```

*(If `xxd` is not available, you can use Python: `python3 -c "import base64; print(base64.b64encode(bytes.fromhex('4D5E6F...')).decode())"`)*

### Add it to Gradle

Copy the resulting Base64 string (e.g., `TV5veoucDR4KOktcbX6Pm...==`) and paste it into your `vrtxCertHash` manifest placeholder. You can provide multiple hashes (e.g., debug and release) separated by commas.

## 4. Launch the SDK

Import the public API and call `Vrtx.setup` from an activity or another UI
event. Store credentials outside source control—for example, inject them through
your build system or use `local.properties` for local development.

```kotlin
import sa.vrtx.public.Vrtx
import sa.vrtx.public.configuration.Environment
import sa.vrtx.public.configuration.Language
import sa.vrtx.public.configuration.Mode

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

`Vrtx.setup` authenticates with Vrtx and launches the SDK activity. It is not a
suspending function. `onSuccess` runs once the SDK UI has launched; use
`onError` to show an integration-safe error state to the user.

## Configuration reference

`Vrtx.setup` accepts these public configuration types:

| Parameter | Type | Values |
| --------- | ---- | ------ |
| `environment` | `Environment` | `Environment.Sandbox`, `Environment.Production` |
| `language` | `Language` | `Language.English`, `Language.Arabic` |
| `mode` | `Mode` | `Mode.LIGHT`, `Mode.DARK` |
| `externalReference` | `String?` | Optional app-defined reference attached to the SDK session |

For appearance, pass `mode` and a Compose `fontFamily` built from a font already embedded in your app, such as Inter.

Omit `externalReference` when no external reference is needed.

## Support

For credentials, license keys, and integration help, contact your Vrtx account manager or [contact@vrtx.sa](mailto:contact@vrtx.sa).

## License

Licensed under the [Apache License, Version 2.0](LICENSE). Copyright © 2026 vrtx fintech.
