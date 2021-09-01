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
                api(kotlinx.coroutines.core)
                api(asoft.logging.console)
                api(project(":live-core"))
                api(project(":kotlinx-coroutines-core"))
            }
        }

        val commonTest by getting {
            dependencies {
                api(asoft.expect.coroutines)
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

        val jvmTest by getting {
            dependencies {
                implementation(kotlinx.coroutines.test)
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
    version = project.version.toString(),
    description = "A multiplatfrom library for authoring viewmodel in an MVIVM architecture"
)