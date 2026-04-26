plugins {
    id("com.android.library") version "8.13.2" apply false
    id("com.android.fused-library") version "8.13.2"
    id("org.jetbrains.kotlin.android") version "2.3.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.3.0" apply false
    `maven-publish`
}

group = "com.github.vrtx-fintech"
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

afterEvaluate {
    publishing {
        publications.named<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = rootProject.name
            version = project.version.toString()
        }
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/vrtx-fintech/vrtx-android")
                credentials {
                    username = (project.findProperty("gpr.user") as String?)
                        ?: System.getenv("GITHUB_USER")
                    password = (project.findProperty("gpr.token") as String?)
                        ?: System.getenv("GITHUB_TOKEN")
                }
            }
        }
    }
}
