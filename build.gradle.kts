plugins {
    id("com.android.library") version "8.13.2" apply false
    id("com.android.fused-library") version "8.13.2"
    id("org.jetbrains.kotlin.android") version "2.3.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.3.0" apply false
    `maven-publish`
}

group = "io.github.abdel-monaam-aouini"
version = (project.findProperty("sdkVersion") as String?) ?: "0.1.0"

androidFusedLibrary {
    namespace = "sa.vrtx.android.fused"
    minSdk = 26
}

dependencies {
    // Compose BOM aligns the unversioned compose deps the embedded SDK pulls in.
    include(platform("androidx.compose:compose-bom:2025.12.01"))
    include(project(":wrapper-src"))
    include("sa.vrtx:sdk:0.0.1")
}

// Fused-library's validateDependencies does POM-only resolution and chokes
// on the SDK's BOM-managed compose deps even though Gradle module metadata
// resolves them correctly. Replace its actions with a no-op that produces
// the empty output dir bundle expects.
tasks.matching { it.name == "validateDependencies" }.configureEach {
    actions.clear()
    doLast {
        outputs.files.forEach { it.mkdirs() }
    }
}

// Maven Central requires sources and javadoc jars; fused-library doesn't
// emit them, so attach empty placeholders. We can't rely on the `base`
// plugin's archive-name conventions (it pulls in LifecycleBasePlugin,
// which collides with fused-library's pre-created `assemble` task), so
// set archiveBaseName/destinationDirectory explicitly.
val sourcesJar by tasks.registering(Jar::class) {
    archiveBaseName.set(rootProject.name)
    archiveClassifier.set("sources")
    destinationDirectory.set(layout.buildDirectory.dir("libs"))
}

val javadocJar by tasks.registering(Jar::class) {
    archiveBaseName.set(rootProject.name)
    archiveClassifier.set("javadoc")
    destinationDirectory.set(layout.buildDirectory.dir("libs"))
}

// Signing and Central Portal upload are handled in the release workflow
// via shell + gpg + curl, because applying the `signing` Gradle plugin
// (or anything that pulls in LifecycleBasePlugin) collides with the
// `assemble` task `com.android.fused-library` pre-creates.
afterEvaluate {
    publishing.publications.named<MavenPublication>("maven") {
        groupId = project.group.toString()
        artifactId = rootProject.name
        version = project.version.toString()
        artifact(sourcesJar)
        artifact(javadocJar)
        pom {
            name.set("vrtx-android")
            description.set("Android SDK for vrtx Pay — onboarding, wallet, and card flows.")
            url.set("https://github.com/vrtx-fintech/vrtx-android")
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            developers {
                developer {
                    id.set("abdel-monaam-aouini")
                    name.set("AbdelMonaam Aouini")
                    email.set("abdelmonaam@vrtx.sa")
                }
            }
            scm {
                url.set("https://github.com/vrtx-fintech/vrtx-android")
                connection.set("scm:git:git://github.com/vrtx-fintech/vrtx-android.git")
                developerConnection.set("scm:git:ssh://git@github.com/vrtx-fintech/vrtx-android.git")
            }
        }
    }
}
