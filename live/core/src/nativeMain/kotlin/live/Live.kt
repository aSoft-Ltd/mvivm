package live

import kotlinx.atomicfu.atomic
import kotlinx.collections.atomic.mutableAtomicListOf

actual class Live<S> actual constructor(state: S) {
    private val atomic = atomic(state)
    actual var value: S
        set(value) {
            atomic.value = value
            for (watcher in watchers) watcher.callable(value)
        }
        get() = atomic.value

    private val watchers = mutableAtomicListOf<Watcher<S>>()

    actual fun watch(callable: (state: S) -> Unit): Watcher<S> = watch(watchers, callable)

    actual fun stop(watcher: Watcher<S>) = watchers.remove(watcher)

    actual fun stopAll() = watchers.clear()
}