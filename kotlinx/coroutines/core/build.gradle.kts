plugins {
    kotlin("multiplatform")
    id("tz.co.asoft.library")
    id("io.codearte.nexus-staging")
    signing
}

kotlin {
    jvm { library(); withJava() }
    js(IR) { library() }
    val nativeTargets = nativeTargets(true)

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(kotlinx.coroutines.core)
            }
        }

        val nativeMain by creating {
            dependsOn(commonMain)
        }

        for (target in nativeTargets) {
            val main by target.compilations.getting {
                defaultSourceSet {
                    dependsOn(nativeMain)
                }
            }

            val test by target.compilations.getting {
                defaultSourceSet {
                    dependsOn(nativeMain)
                    dependsOn(main.defaultSourceSet)
                }
            }
        }
    }
}

aSoftOSSLibrary(
    version = asoft.versions.mvivm.get(),
    description = "A Patch to provide universal dispatchers"
)