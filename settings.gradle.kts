pluginManagement {
    enableFeaturePreview("VERSION_CATALOGS")
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.android") {
                useModule("com.android.tools.build:gradle:${requested.version}")
            }
        }
    }

    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }

    dependencyResolutionManagement {
        versionCatalogs {
            file("gradle/versions").listFiles().map { it.nameWithoutExtension }.forEach {
                create(it) { from(files("gradle/versions/$it.toml")) }
            }
        }
    }
}

fun includeRoot(name: String, path: String) {
    include(":$name")
    project(":$name").projectDir = File(path)
}

fun includeSubs(base: String, path: String = base, vararg subs: String) {
    subs.forEach {
        include(":$base-$it")
        project(":$base-$it").projectDir = File("$path/$it")
    }
}

rootProject.name = "mvivm"

includeSubs("live", "live", "core", "react", "coroutines")
//includeSubs("live", "live", "core", "react", "compose", "coroutines")
includeSubs("viewmodel", "viewmodel", "core", "react", "coroutines")
//includeSubs("viewmodel", "viewmodel", "core", "react", "compose", "coroutines")
includeSubs("viewmodel-test", "viewmodel/test", "core", "expect")