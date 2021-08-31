import org.gradle.kotlin.dsl.asoft

subprojects {
    afterEvaluate {
        group = "tz.co.asoft"
        version = asoft.versions.mvivm.get()
    }
}