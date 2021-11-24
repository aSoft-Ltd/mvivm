import org.jetbrains.compose.compose

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("tz.co.asoft.library")
    signing
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

android {
    configureAndroid("src/androidMain")
}

kotlin {
    android { library() }
    jvm { library() }
    js(IR) { library() }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":live-core"))
                implementation(compose.runtime)
            }
        }
    }
}

aSoftOSSLibrary(
    version = asoft.versions.mvivm.get(),
    description = "Bindings for Live<S> object to be used with compose"
)