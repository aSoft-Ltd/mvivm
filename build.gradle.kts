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

val createRelease by tasks.creating {
    doLast {
        exec {
            commandLine(
                "curl",
                "-X", "POST",
                "-H", "application/vnd.github.v3+json",
                "-H", "Authorization: token ${System.getenv("GH_TOKEN")}",
                "https://api.github.com/repos/aSoft-Ltd/mvivm/releases",
                "-d", """{"tag_name":"v0.0.1","name":"Version 0.0.1" }""",
            )
        }
    }
}