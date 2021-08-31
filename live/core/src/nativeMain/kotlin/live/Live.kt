package live

import kotlinx.atomic.collections.mutableAtomicListOf
import kotlinx.atomicfu.atomic

actual class Live<T> actual constructor(v: T) {
    private val atomic = atomic(v)
    actual var value: T
        set(value) {
            atomic.value = value
            for (watcher in watchers) watcher.callable(value)
        }
        get() = atomic.value

    private val watchers = mutableAtomicListOf<Watcher<T>>()

    actual fun watch(callable: (T) -> Unit): Watcher<T> = watch(watchers, callable)

    actual fun stop(watcher: Watcher<T>) = watchers.remove(watcher)

    actual fun stopAll() = watchers.clear()
}