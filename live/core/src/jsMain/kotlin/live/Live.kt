package live

import kotlin.js.JsExport

@JsExport
actual class Live<T> actual constructor(v: T) {
    @JsName("value")
    actual var value: T = v
        set(value) {
            field = value
            for (watcher in watchers) watcher.callable(value)
        }

    private val watchers = mutableListOf<Watcher<T>>()

    @JsName("watch")
    actual fun watch(callable: (T) -> Unit): Watcher<T> = watch(watchers, callable)

    @JsName("stop")
    actual fun stop(watcher: Watcher<T>) = watchers.remove(watcher)

    @JsName("stopAll")
    actual fun stopAll() = watchers.clear()
}