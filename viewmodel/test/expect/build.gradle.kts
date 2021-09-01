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
                api(project(":viewmodel-test-core"))
                api(project(":kotlinx-coroutines-test"))
                api(asoft.expect.coroutines)
                api(asoft.kotlinx.atomic.collections)
            }
        }
    }
}

aSoftOSSLibrary(
    version = project.version.toString(),
    description = "A multiplatfrom library to help test viewmodels with the expect library"
)