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
    implementation("sa.vrtx.sa:vrtx-android:0.1.1")
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
## Resolve manifest merger conflicts
The SDK enforces strict security defaults by disabling app backups and cleartext HTTP traffic (android:allowBackup="false", android:fullBackupContent="false", and android:usesCleartextTraffic="false") to prevent sensitive data extraction and man-in-the-middle attacks.

If your app currently enables backups or cleartext traffic (for example, android:allowBackup="true", android:fullBackupContent="@xml/backup_rules", or android:usesCleartextTraffic="true"), the Gradle manifest merger will fail with conflicts like:
```
Attribute application@allowBackup value=(true) from AndroidManifest.xml
is also present at [sa.vrtx.sa:vrtx-android:0.1.1] AndroidManifest.xml value=(false).

Attribute application@fullBackupContent value=(@xml/backup_rules) from AndroidManifest.xml
is also present at [sa.vrtx.sa:vrtx-android:0.1.1] AndroidManifest.xml value=(false).

Attribute application@usesCleartextTraffic value=(true) from AndroidManifest.xml
is also present at [sa.vrtx.sa:vrtx-android:0.1.1] AndroidManifest.xml value=(false).
```
To resolve this and align with the SDK's security requirements, update your app's AndroidManifest.xml to explicitly disable backups and cleartext traffic, and remove any custom backup rules:

```xml
<!-- app/src/main/AndroidManifest.xml -->
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

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
> _Note: If you need to enforce other conflicting attributes on your <application> tag, you can use tools:replace="android:allowBackup" but ensure you keep the values set to false._

## Generating your Certificate Hash

The SDK uses the certificate hash to verify app integrity and prevent repackaging. freeRASP requires the **SHA-256** hash of your signing certificate, converted to **Base64** format.

**1. Get the SHA-256 fingerprint**

Open your terminal and run the following `keytool` command:

```bash
keytool -list -v -keystore path/to/your/keystore.jks -alias your_alias
```

*(For the standard debug keystore, the path is `~/.android/debug.keystore`, the alias is `androiddebugkey`, and the password is `android`)*.

Enter your keystore password when prompted. Look for the `SHA256:` fingerprint in the output. It will look like this:

```text
SHA256: 4D:5E:6F:7A:8B:9C:0D:1E:2F:3A:4B:5C:6D:7E:8F:9A:0B:1C:2D:3E:4F:5A:6B:7C:8D:9E:0F:1A:2B:3C:4D:5E
```

**2. Convert the hex string to Base64**

Run this command (replace the hex string with your own from the previous step):

```bash
echo -n "4D:5E:6F:7A:8B:9C:0D:1E:2F:3A:4B:5C:6D:7E:8F:9A:0B:1C:2D:3E:4F:5A:6B:7C:8D:9E:0F:1A:2B:3C:4D:5E" | tr -d ':' | xxd -r -p | base64
```

*(If `xxd` is not available, you can use Python: `python3 -c "import base64; print(base64.b64encode(bytes.fromhex('4D5E6F...')).decode())"`)*

**3. Add it to your Gradle file**

Copy the resulting Base64 string (e.g., `TV5veoucDR4KOktcbX6Pm...==`) and paste it into your `vrtxCertHash` manifest placeholder. You can provide multiple hashes (e.g., debug and release) separated by commas.

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
| `environment` | `Environment` | `Environment.Sandbox`, `Environment.Production` |
| `language` | `Language` | `Language.English`, `Language.Arabic` |
| `mode` | `Mode` | `Mode.LIGHT`, `Mode.DARK` |
| `externalReference` | `String?` | Optional app-defined reference attached to the SDK session |

For appearance, pass `mode` and a Compose `fontFamily` built from a font already embedded in your app, such as Inter.

Omit `externalReference` when no external reference is needed. The example app sends a generated UUID string.

## Support

For credentials, license keys, and integration help, contact your Vrtx account manager or [support@vrtx.sa](mailto:support@vrtx.sa).

## License

Licensed under the [Apache License, Version 2.0](LICENSE). Copyright © 2026 vrtx fintech.
