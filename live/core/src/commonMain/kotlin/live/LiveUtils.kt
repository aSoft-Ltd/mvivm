package live

internal fun <T> Live<T>.watch(watchers: MutableList<Watcher<T>>, callable: (T) -> Unit): Watcher<T> {
    val watcher = Watcher(callable, watchers)
    watchers.add(watcher)
    callable(value)
    return watcher
}