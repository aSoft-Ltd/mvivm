import org.gradle.kotlin.dsl.asoft

plugins {
    id("com.android.library") apply false
    kotlin("multiplatform") apply false
    kotlin("js") apply false
    id("tz.co.asoft.library") apply false
    id("io.codearte.nexus-staging") apply false
}

subprojects {
    afterEvaluate {
        group = "tz.co.asoft"
        version = asoft.versions.mvivm.get()
    }
}