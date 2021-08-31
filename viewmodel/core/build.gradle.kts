import org.jetbrains.kotlin.gradle.plugin.KotlinTarget

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("tz.co.asoft.library")
    id("io.codearte.nexus-staging")
    signing
}

kotlin {
    android { library() }
    jvm { library() }
    js(IR) { library() }
    val nativeTargets = nativeTargets(true)
    sourceSets {
        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${vers.kotlinx.coroutines}")
                api(asoft("logging-console", vers.asoft.logging))
                api(asoft("live-core", vers.asoft.live))
            }
        }

        val commonTest by getting {
            dependencies {
                api(asoft("expect-coroutines", vers.asoft.expect))
            }
        }

        val androidMain by getting {
            dependencies {
                api("androidx.lifecycle:lifecycle-viewmodel-ktx:${vers.androidx.lifecycle}")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-android:${vers.kotlinx.coroutines}")
            }
        }

        val nonAndroidMain by creating {
            dependsOn(commonMain)
        }

        val nonAndroidTest by creating {
            dependsOn(nonAndroidMain)
            dependsOn(commonTest)
        }

        val jvmMain by getting {
            dependsOn(nonAndroidMain)
        }

        val jvmTest by getting {
            dependencies {
                api(kotlinx("coroutines-test", vers.kotlinx.coroutines))
            }
        }

        val jsMain by getting {
            dependsOn(nonAndroidMain)
        }
        
        for (target in nativeTargets) {
            val main by target.compilations.getting {
                defaultSourceSet {
                    dependsOn(nonAndroidMain)
                }
            }

            val test by target.compilations.getting {
                defaultSourceSet {
                    dependsOn(main.defaultSourceSet)
                }
            }
        }
    }
}

aSoftOSSLibrary(
    version = vers.asoft.viewmodel,
    description = "A multiplatfrom library to handling viewmodel in an MVIVM architecture"
)