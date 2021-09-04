import org.gradle.kotlin.dsl.asoft
import java.text.SimpleDateFormat
import java.util.*

plugins {
    alias(androidx.plugins.library) apply false
    alias(jetbrains.plugins.kotlin.multiplatform) apply false
    alias(asoft.plugins.library) apply false
    alias(nexus.plugins.publish) apply false
}

allprojects {
    afterEvaluate {
        group = "tz.co.asoft"
        version = asoft.versions.mvivm.get()
    }
}

val releases = file("Release.next.md").readText()

val prepareChangelog by tasks.creating {
    doFirst {
        val changelog = file("CHANGELOG.md")
        val changelogText = changelog.readText()
        changelog.apply {
            writeText("# Version $version : ${SimpleDateFormat("yyyy-MM-dd").format(Date())}\n\n")
            appendText("$releases\n\n")
            appendText(changelogText)
        }
    }
}

val beginDeploymentPipeline by tasks.creating {
    dependsOn(prepareChangelog)
    doLast {
        exec { commandLine("git", "add", ".") }
        exec { commandLine("git", "commit", "-m", "Releasing $version") }
        exec { commandLine("git", "push", "origin", "master") }
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
                "-d", """{ "tag_name":"v$version", "name":"Version $version", "body":"${releases.replace("\n", "\\n")}" }""",
            )
        }
    }
}