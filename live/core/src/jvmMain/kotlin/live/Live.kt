package live

actual class Live<T> actual constructor(v: T) {
    actual var value: T = v
        set(value) {
            field = value
            for (watcher in watchers) watcher.callable(value)
        }

    private val watchers = mutableListOf<Watcher<T>>()

    @JvmSynthetic
    actual fun watch(callable: (T) -> Unit): Watcher<T> = watch(watchers, callable)

    fun watch(callable: Callback<T>): Watcher<T> = watch(watchers, callable::execute)

    actual fun stop(watcher: Watcher<T>) = watchers.remove(watcher)

    actual fun stopAll() = watchers.clear()
}