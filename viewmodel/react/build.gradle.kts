plugins {
    kotlin("js")
    id("tz.co.asoft.library")
    id("io.codearte.nexus-staging")
    signing
}

kotlin {
    js(IR) { library() }
    sourceSets {
        val main by getting {
            dependencies {
                api(asoft.reakt.core)
                api(project(":viewmodel-core"))
                api(project(":live-react"))
            }
        }

        val test by getting {
            dependencies {
                implementation(asoft.expect.core)
            }
        }
    }
}

aSoftOSSLibrary(
    version = asoft.versions.mvivm.get(),
    description = "A kotlin/js library to handle viewmodels in an MVVMI architecture from react"
)