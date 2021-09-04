pluginManagement {
    enableFeaturePreview("VERSION_CATALOGS")
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.android") {
                useModule("com.android.tools.build:gradle:${requested.version}")
            }
        }
    }

    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }

    dependencyResolutionManagement {
        versionCatalogs {
            listOf("androidx", "asoft", "jetbrains", "kotlinx", "nexus").forEach {
                create(it) { from(files("gradle/versions/$it.toml")) }
            }
        }
    }
}

rootProject.name = "mvivm"

include(":live-core")
project(":live-core").projectDir = File("live/core")

include(":live-react")
project(":live-react").projectDir = File("live/react")

include(":viewmodel-core")
project(":viewmodel-core").projectDir = File("viewmodel/core")

include(":viewmodel-react")
project(":viewmodel-react").projectDir = File("viewmodel/react")

include(":viewmodel-test-core")
project(":viewmodel-test-core").projectDir = File("viewmodel/test/core")

include(":viewmodel-test-expect")
project(":viewmodel-test-expect").projectDir = File("viewmodel/test/expect")