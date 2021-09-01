plugins {
    kotlin("multiplatform")
    id("tz.co.asoft.library")
    id("io.codearte.nexus-staging")
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
    version = project.version.toString(),
    description = "A multiplatfrom library to help test viewmodels"
)