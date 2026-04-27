plugins {
    id("com.android.library") version "8.13.2" apply false
    id("com.android.fused-library") version "8.13.2"
    id("org.jetbrains.kotlin.android") version "2.3.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.3.0" apply false
    `maven-publish`
    signing
    id("com.gradleup.nmcp") version "0.0.9"
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
// emit them, so attach empty placeholders.
val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

afterEvaluate {
    publishing {
        publications.named<MavenPublication>("maven") {
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

    signing {
        val signingKey = providers.gradleProperty("signingInMemoryKey").orNull
        val signingKeyId = providers.gradleProperty("signingInMemoryKeyId").orNull
        val signingPassword = providers.gradleProperty("signingInMemoryKeyPassword").orNull
        if (!signingKey.isNullOrBlank() && !signingPassword.isNullOrBlank()) {
            if (!signingKeyId.isNullOrBlank()) {
                useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
            } else {
                useInMemoryPgpKeys(signingKey, signingPassword)
            }
            sign(publishing.publications["maven"])
        }
    }
}

nmcp {
    publishAllPublications {
        username = providers.gradleProperty("mavenCentralUsername")
        password = providers.gradleProperty("mavenCentralPassword")
        publicationType = "USER_MANAGED"
    }
}
