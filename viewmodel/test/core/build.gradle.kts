plugins {
    kotlin("multiplatform")
    id("tz.co.asoft.library")
    signing
}

kotlin {
    jvm { library() }
    js(IR) { library() }
    nativeTargets(true)
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":viewmodel-core"))
            }
        }
    }
}

aSoftOSSLibrary(
    version = asoft.versions.mvivm.get(),
    description = "A multiplatfrom library to help test viewmodels"
)