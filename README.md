# vrtx-android

The official Android SDK for **Vrtx** — onboarding, wallet, and card flows for your app.

## Requirements

| Tooling               | Minimum                        |
| --------------------- | ------------------------------ |
| Android `minSdk`      | 29                             |
| Android `compileSdk`  | 37                             |
| Android Gradle Plugin | 9.4.1                          |
| Gradle                | 9.6.0                          |
| Kotlin                | 2.4.20                         |
| JVM target            | 17 (the example app builds at 21) |

The SDK is compiled against JVM target 17. A higher target, such as the 21 used by
`example/`, is fine as long as it is at least 17.

## 1. Add the SDK

Declare the dependency and add the repositories it resolves from.

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

`mavenCentral()` and `jitpack.io` are both load-bearing and neither can be
dropped. 

The SDK requires `compileSdk` 37 or higher.

## 2. Configure the manifest placeholders

The SDK reads the expected app identity from two manifest placeholders. Derive the
package name from `applicationId`

```kotlin
// app/build.gradle.kts
android {
    defaultConfig {
        manifestPlaceholders["vrtxPackageName"] = applicationId ?: ""
        manifestPlaceholders["vrtxCertHash"] = "YOUR_CERT_HASH"
    }
}
```

`vrtxPackageName` must equal the **final** application ID, any suffix is included. Also — see
[Generate your certificate hash](#6-generate-your-certificate-hash).

## 3. Align manifest security settings

The SDK enforces strict security defaults: backups and cleartext HTTP traffic
are disabled. If your app currently enables either, the manifest merger will
report a conflict.

For example:

```
Attribute application@allowBackup value=(true) from AndroidManifest.xml
is also present at [sa.vrtx.sa:vrtx-android:0.1.13] AndroidManifest.xml value=(false).

Attribute application@fullBackupContent value=(@xml/backup_rules) from AndroidManifest.xml
is also present at [sa.vrtx.sa:vrtx-android:0.1.13] AndroidManifest.xml value=(false).

Attribute application@usesCleartextTraffic value=(true) from AndroidManifest.xml
is also present at [sa.vrtx.sa:vrtx-android:0.1.13] AndroidManifest.xml value=(false).
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

## 4. Permissions

You do not need to declare any of these yourself — they arrive through manifest
merge. You do need to account for them.

**Declared by the SDK:**

| Permission                                          | Why the SDK needs it                          |
| --------------------------------------------------- | --------------------------------------------- |
| `INTERNET`                                          | API traffic                                  |
| `ACCESS_NETWORK_STATE`                              | Connectivity checks                          |
| `READ_PHONE_STATE`                                  | Device binding and integrity signals         |
| `USE_BIOMETRIC`                                     | Consumer authentication                      |
| `READ_GSERVICES`                                    | Google Play Services device integrity signals |
| `ACCESS_WIFI_STATE`                                 | Network integrity signals                    |
| `ACCESS_COARSE_LOCATION`                            | Emulator and automation detection            |
| `ACCESS_FINE_LOCATION`                              | Emulator and automation detection            |
| `DETECT_SCREEN_CAPTURE`                             | Blocks capture of cardholder data            |
| `DETECT_SCREEN_RECORDING`                           | Blocks recording of cardholder data          |
| `NFC`                                               | EMV card reading (optional)                   |

**Pulled in transitively by the SDK's own dependencies:**

| Permission                                     | Arrives via                | Why the SDK needs it                    |
| ---------------------------------------------- | -------------------------- | --------------------------------------- |
| `com.google.android.gms.permission.AD_ID`      | `play-services-ads-identifier` | Device fingerprinting (fingerprintjs) |
| `RECEIVE_BOOT_COMPLETED`                       | `truetime-android`         | Trusted time source for integrity checks |
| `USE_FINGERPRINT`                              | `fingerprint-android`      | Legacy alias of `USE_BIOMETRIC`         |

The transitive ones are easy to miss because they appear nowhere in the SDK's
source. `AD_ID` in particular grants access to the advertising identifier, which
is a data-safety disclosure item in the Play Console even though the SDK never
uses it for advertising.

NFC is declared with `<uses-feature android.hardware.nfc android:required="false" />`,
so it does not exclude devices without NFC from installing.

`ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`, `READ_PHONE_STATE`,
`USE_BIOMETRIC`, and `AD_ID` are sensitive. They are merged in whether or not
your app requests a runtime permission, so they must be declared in your Play
Console data safety form and explained in your privacy policy. Verify the final
set for your own dependency tree before you publish, because transitive
permissions can change between SDK releases:

```bash
./gradlew :app:assembleSandboxDebug
AAPT2=$(ls -d "$ANDROID_HOME"/build-tools/*/ | sort -V | tail -1)aapt2
"$AAPT2" dump permissions app/build/outputs/apk/sandbox/debug/app-sandbox-debug.apk
```

## 5. AndroidX Startup must stay enabled

The SDK initialises through `androidx.startup:startup-runtime`, which arrives
transitively, and registers an `androidx.startup.InitializationProvider` entry in
its manifest.

If your app strips AndroidX Startup to cut cold-start cost — by removing
`androidx.startup.InitializationProvider` with `tools:node="remove"`, or by any
other means — the SDK never initialises and every `Vrtx.setup` call fails with
`InternalAndroidxStartupError`. Do not remove the provider.

## 6. Generate your certificate hash

The SDK uses the certificate hash to verify app integrity and prevent repackaging. freeRASP requires the **SHA-256** hash of your signing certificate, converted to **Base64** format.

### Get the SHA-256 fingerprint

Open your terminal and run the following `keytool` command:

```bash
keytool -list -v -keystore path/to/your/keystore.jks -alias your_alias
```

_(For the standard debug keystore, the path is `~/.android/debug.keystore`, the alias is `androiddebugkey`, and the password is `android`)_.

Enter your keystore password when prompted. Look for the `SHA256:` fingerprint in the output. It will look like this:

```text
SHA256: 4D:5E:6F:7A:8B:9C:0D:1E:2F:3A:4B:5C:6D:7E:8F:9A:0B:1C:2D:3E:4F:5A:6B:7C:8D:9E:0F:1A:2B:3C:4D:5E
```

### Convert the hex string to Base64

Run this command (replace the hex string with your own from the previous step):

```bash
echo -n "4D:5E:6F:7A:8B:9C:0D:1E:2F:3A:4B:5C:6D:7E:8F:9A:0B:1C:2D:3E:4F:5A:6B:7C:8D:9E:0F:1A:2B:3C:4D:5E" | tr -d ':' | xxd -r -p | base64
```

_(If `xxd` is not available, you can use Python: `python3 -c "import base64; print(base64.b64encode(bytes.fromhex('4D5E6F...')).decode())"`)_

### Add it to Gradle

Copy the resulting Base64 string (e.g., `TV5veoucDR4KOktcbX6Pm...==`) and paste it into your `vrtxCertHash` manifest placeholder. You can provide multiple hashes (e.g., debug and release) separated by commas, which lets one build serve both signing identities.

## 7. Launch the SDK

Import the public API and call `Vrtx.setup` from an activity or another UI
event. Store credentials outside source control—for example, inject them through
your build system or use `local.properties` for local development.

```kotlin
import android.net.Uri
import androidx.compose.ui.graphics.Color
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
    brandName = "Atlas Pay",
    colors = VrtxColors(
        allBrands = VrtxColors.AllBrands(
            primary = Color(0xFF377DFF),
            buttonLabel = Color(0xFFFFFFFF),
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
                secondary = Color(0xFF4DE3D1),
            ),
        ),
        backgrounds = VrtxColors.Backgrounds(
            primary = Color(0xFFF4F8FF),
            secondary = Color(0xFFF7FAFF),
        ),
        backgroundsGradient = VrtxColors.BackgroundsGradients(
            wb01 = Color(0xFFEAF3FF),
            wb02 = Color(0xFFE7F5F6),
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
    clientId = "VRTX_CLIENT_ID",
    clientSecret = "VRTX_CLIENT_SECRET",
    environment = Environment.Sandbox,
    language = Language.English,
    designOption = DesignOption.OptionC,
    mode = Mode.LIGHT,
    theme = customThemeOptions,
    fontFamily = FontFamily.Default,
    externalReference = "YOUR_EXTERNAL_REFERENCE",
    onSuccess = { /* SDK UI launched */ },
    onError = { error -> /* surface to the user */ },
    onExit = { /* SDK UI closed */ },
)
```

`Vrtx.setup` authenticates with Vrtx and launches the SDK activity. It is not a
suspending function and returns immediately; the work happens on an internal
coroutine scope. Only one session can be active at a time — see
[Callback contract](#callback-contract).

`fontFamily` is **required** (it has no default) and must resolve to a font your
app actually embeds, such as Inter. Omitting it is a compile error, not a
fallback.

## Configuration reference

`Vrtx.setup` accepts these public configuration types:

| Parameter           | Type                    | Required | Values                                                                 |
| ------------------- | ----------------------- | -------- | ---------------------------------------------------------------------- |
| `clientId`          | `String`                | Yes      | Issued by Vrtx                                                        |
| `clientSecret`      | `String`                | Yes      | Issued by Vrtx                                                        |
| `environment`       | `Environment`           | Yes      | `Sandbox`, `Staging`, `Production`                                    |
| `fontFamily`        | `FontFamily`            | Yes      | A `FontFamily` from a font embedded in your app                       |
| `language`          | `Language`              | No       | `English` (default), `Arabic`                                         |
| `designOption`      | `DesignOption`          | No       | `OptionA`, `OptionB`, `OptionC` (default)                             |
| `mode`              | `Mode`                  | No       | `LIGHT` (default), `DARK`                                             |
| `theme`             | `ThemeOptions?`         | No       | Optional SDK theme and design-token overrides                          |
| `externalReference` | `String?`               | No       | Optional app-defined reference attached to the SDK session             |
| `onSuccess`         | `() -> Unit`            | No       | Fires once the SDK activity has been launched                          |
| `onError`           | `(VrtError) -> Unit`    | No       | See [Error reference](#error-reference)                               |
| `onExit`            | `() -> Unit`            | No       | Fires when the user leaves the SDK UI                                 |

`Environment.Staging` exists alongside Sandbox and Production, and is the
environment to validate against before shipping.

### ThemeOptions reference

| Parameter        | Type           | Values                                                                          |
| ---------------- | -------------- | ------------------------------------------------------------------------------- |
| `cardImage`      | `Uri?`         | Optional card image URI                                                         |
| `brandLogo`      | `Uri?`         | Optional brand logo URI                                                         |
| `brandName`      | `String?`      | Optional brand name                                                             |
| `colors`         | `VrtxColors?`  | `allBrands`, `labels`, `fills`, `backgrounds`, `backgroundsGradient`, `accents` |
| `spacing`        | `VrtxSpacing?` | `x0`, `xxs`, `xs`, `sm`, `md`, `ml`, `lg`                                       |
| `radius`        | `VrtxRadius?`  | `s`, `sm`, `md`, `lg`, `full`, `huge`                                           |

`VrtxColors` contains these nested keys:

| **GroupTypeKeys**            | **Type**                          | **Keys**                                                              |
| ---------------------------- | --------------------------------- | --------------------------------------------------------------------- |
| `colors.allBrands`           | `VrtxColors.AllBrands`            | `primary`, `buttonLabel`                                              |
| `colors.labels`              | `VrtxColors.Labels`               | `primary`, `secondary`, `tertiary`, `quaternary`                      |
| `colors.fills`               | `VrtxColors.Fills`                | `primary`, `secondary`, `tertiary`, `quaternary`, `vibrant.secondary` |
| `colors.backgrounds`         | `VrtxColors.Backgrounds`          | `primary`, `secondary`                                                |
| `colors.backgroundsGradient` | `VrtxColors.BackgroundsGradients` | `wb01`, `wb02`                                                        |
| `colors.accents`             | `VrtxColors.Accents`              | `red`, `green`, `greenBg`                                             |

`Vrtx.defaultThemeOptions()` returns a complete theme with the SDK's default
colors, spacing, and radius tokens. Consumers may use it as a base and
copy or replace any `ThemeOptions` property, or construct `ThemeOptions`
directly. Every nested token is nullable and falls back to the SDK default when
omitted, so you can override only the tokens you own.

The `customThemeOptions` object above is passed as `theme` in `Vrtx.setup`.
The nested color and token objects are also public (`VrtxColors`,
`VrtxSpacing`, and `VrtxRadius`) when an app needs to replace
the complete design system.

## Error reference

`onError` receives a `VrtError`. Match on the concrete subtype to decide what to
tell the user.

| Error                          | Meaning                                                                | What to do                                                                 |
| ------------------------------ | ---------------------------------------------------------------------- | -------------------------------------------------------------------------- |
| `AuthenticationError`          | Backend rejected the credentials                                       | Check `clientId` / `clientSecret` and that the app identity is registered   |
| `SecurityVerificationError`   | Device integrity check blocked entry                                   | Read `threatCode`; the SDK has already shown its own explanation screen     |
| `SdkAlreadyActiveError`        | `setup` was called while a session is already launching or open        | Guard your entry point; see [Callback contract](#callback-contract)        |
| `InternalAndroidxStartupError` | SDK did not initialise                                                  | You removed AndroidX Startup; restore it                                    |
| `InsecureEnvironmentError`     | Cleartext traffic permitted in a non-debuggable build                  | See the note below                                                        |

`SecurityVerificationError.threatCode` is a stable string suitable for logging
and crash reporting. It defaults to `SECURITY_VERIFICATION_FAILED`; the values
include `ROOT`, `HOOK`, `TAMPER`, `DEBUGGER`, `EMULATOR`, `DEVICE_BINDING`,
`AUTOMATION`, `SCREEN_CAPTURE`, `SCREEN_RECORDING`, `UNSECURE_WIFI`,
`TIME_SPOOFING`, `LOCATION_SPOOFING`, `MALWARE`, `MULTI_INSTANCE`, `VPN`,
`UNLOCKED_DEVICE`, `OBFUSCATION_ISSUES`, `DEVELOPER_MODE`, `ADB_ENABLED`,
`UNTRUSTED_INSTALLATION`, `HARDWARE_BACKED_KEYSTORE_UNAVAILABLE`, and
`SECURITY_VERIFICATION_FAILED`.

> **`InsecureEnvironmentError` only fires in non-debuggable builds.** The check is
> skipped when `FLAG_DEBUGGABLE` is set, so a debug build will run fine and the
> release build will fail. The SDK checks `usesCleartextTraffic` at runtime; if
> your app uses a `network_security_config.xml`, it must also set
> `cleartextTrafficPermitted="false"`, which is what the error message points at.
> Cardholder data requires TLS under PCI DSS 4.2.1.

## Callback contract

`Vrtx.setup` runs an internal state machine, and the callbacks do not map
one-to-one onto a success/failure pair:

```
IDLE ──setup()──> SETTING_UP ──> SDK_SCREEN ──user exits──> IDLE
                      │
                      ├──integrity blocked──> SECURITY_SCREEN ──user closes──> IDLE
                      └──already active──> IDLE (onError only)
```

- `onSuccess` fires **after** the SDK activity has been started, not after the
  session is authenticated. Use it to clear a loading indicator.
- `onError` can fire **while the SDK's own security screen is showing**. Treat it
  as "the SDK could not open a session", not "the app is now in a terminal error
  state" — otherwise you will render an error on top of the SDK's explanation
  screen.
- `onExit` fires when the user leaves the SDK UI, including from the security
  screen.
- A second `setup` call while the state machine is not `IDLE` fails fast with
  `SdkAlreadyActiveError` and does nothing else. Disable your entry point while
  a session is launching, so a double tap does not surface this to users.
- Reset your loading state in all three callbacks. Every session ends in exactly
  one of them.

## Device integrity and environment policy

The SDK checks device integrity and blocks entry when a threat is detected. The
policy differs per environment, and the allowances are deliberately narrow.

| Detection                                                      | Sandbox | Staging | Production |
| -------------------------------------------------------------- | ------- | ------- | ---------- |
| Debugger attached, obfuscation warnings                         | waived  | blocked | blocked    |
| `TAMPER` — app ID or signing certificate mismatch              | waived  | blocked | blocked    |
| Unlocked device, Developer Mode, ADB enabled                    | waived  | blocked | blocked    |
| Unsecured WiFi, missing StrongBox key                           | waived  | blocked | blocked    |
| RASP provider verification failure                              | waived  | blocked | blocked    |
| Emulator                                                       | waived  | waived  | blocked    |
| Untrusted (unofficial store) installation                       | waived  | waived  | waived     |
| Root, hook, automation, malware, multi-instance                 | blocked | blocked | blocked    |
| Screen capture, screen recording, device binding, time spoofing | blocked | blocked | blocked    |
| Location spoofing, system VPN                                   | blocked | blocked | blocked    |

Two rows deserve emphasis:

- **`TAMPER` is waived in Sandbox,** and it
  does not run during development.
- **Emulators are allowed in Sandbox and Staging but not Production.** You can
  develop and QA without a physical device, but Production requires real
  hardware. The emulator-specific waivers exist because every emulator reports
  an open virtual access point and has no StrongBox element.

`CANNOT_ATTEST_IDS`-style device-ID attestation failures on an emulator are an
emulator KeyMint limitation, not an integration error; test attestation on real
hardware before diagnosing it.

## Build the example

`example/` is a standalone Gradle build that consumes the published SDK from
Maven Central. It uses `com.atlaspay.app` as its application ID.

```bash
cd example
./gradlew :app:assembleSandboxDebug     # or :app:assembleSandboxRelease
```

Both variant names are what CI builds, so keep them working. Override the SDK
version with a project property:

```bash
./gradlew :app:assembleSandboxDebug -PsdkVersion=0.1.12
```

The example reads its configuration from `local.properties` or the environment.
`local.properties` is gitignored; never commit these values.

| Key                       | Purpose                                          |
| ------------------------- | ------------------------------------------------ |
| `VRTX_CLIENT_ID`          | Client ID issued by Vrtx                         |
| `VRTX_CLIENT_SECRET`      | Client secret issued by Vrtx                     |
| `VRTX_ENVIRONMENT`        | `Sandbox`, `Staging`, or `Production`            |
| `VRTX_CERT_HASH`          | Base64 SHA-256 hash of the signing certificate   |
| `ANDROID_KEYSTORE_FILE`   | Release keystore path; falls back to debug signing when unset |
| `ANDROID_KEYSTORE_PASSWORD` | Release keystore password                     |
| `ANDROID_KEY_ALIAS`       | Release key alias                                |
| `ANDROID_KEY_PASSWORD`    | Release key password                             |

## Support

For credentials, license keys, and integration help, contact your Vrtx account manager or [contact@vrtx.sa](mailto:contact@vrtx.sa).

## License

Licensed under the [Apache License, Version 2.0](LICENSE). Copyright © 2026 vrtx fintech.
