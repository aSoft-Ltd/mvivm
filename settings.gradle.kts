pluginManagement {
    val vers = object {
        val agp = "7.1.0-alpha05"
        val kotlin = "1.5.10"
        val nexus_staging = "0.22.0"

        val kotlinx = object {
            val coroutines = "1.5.0-native-mt"
        }

        val androidx = object {
            val lifecycle = "2.4.0-alpha02"
        }

        val asoft = object {
            val mvivm = "0.1.0"
            val collections = "0.0.10"
            val logging = "0.0.33"
            val expect = "0.0.42"
            val test = "1.1.40"
            val reakt = "0.1.10"
            val builders = "1.3.42"
        }
    }
    enableFeaturePreview("VERSION_CATALOGS")
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.android") {
                useModule("com.android.tools.build:gradle:${vers.agp}")
            }
        }
    }

    plugins {
        id("com.android.library") version vers.agp apply false
        kotlin("multiplatform") version vers.kotlin apply false
        kotlin("js") version vers.kotlin apply false
        id("tz.co.asoft.library") version vers.asoft.builders apply false
        id("io.codearte.nexus-staging") version vers.nexus_staging apply false
    }

    repositories {
        google()
        jcenter()
        gradlePluginPortal()
        mavenCentral()
    }

    dependencyResolutionManagement {
        versionCatalogs {
            create("asoft") {
                val group = "tz.co.asoft"
                version("mvivm", vers.asoft.mvivm)
                alias("expect-core").to(group, "expect-core").version(vers.asoft.expect)
                alias("expect-coroutines").to(group, "expect-coroutines").version(vers.asoft.expect)
                alias("kotlinx-atomic-collections").to(group, "kotlinx-atomic-collections")
                    .version(vers.asoft.collections)
                alias("logging-console").to(group, "logging-console").version(vers.asoft.logging)
                alias("reakt-core").to(group, "reakt-core").version(vers.asoft.reakt)
            }

            create("kotlinx") {
                val group = "org.jetbrains.kotlinx"
                alias("coroutines-core").to(group, "kotlinx-coroutines-core")
                    .version(vers.kotlinx.coroutines)
                alias("coroutines-android").to(group, "kotlinx-coroutines-android")
                    .version(vers.kotlinx.coroutines)
                alias("coroutines-test").to(group, "kotlinx-coroutines-test")
                    .version(vers.kotlinx.coroutines)
            }

            create("androidx") {
                alias("lifecycle-viewmodel-ktx").to("androidx.lifecycle", "lifecycle-viewmodel-ktx")
                    .version(vers.androidx.lifecycle)
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