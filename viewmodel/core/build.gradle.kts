plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("tz.co.asoft.library")
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
                api(kotlinx.coroutines.core)
                api(asoft.logging.console)
                api(project(":live-core"))
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(project(":viewmodel-test-expect"))
                implementation(asoft.kotlinx.coroutines.test)
                implementation(asoft.expect.coroutines)
            }
        }

        val androidMain by getting {
            dependencies {
                api(androidx.lifecycle.viewmodel.ktx)
                api(kotlinx.coroutines.android)
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
    version = asoft.versions.mvivm.get(),
    description = "A multiplatfrom library for authoring viewmodel in an MVIVM architecture"
)