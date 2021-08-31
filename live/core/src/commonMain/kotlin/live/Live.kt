package live

expect class Live<T>(v: T) {
    var value: T
    fun watch(callable: (T) -> Unit): Watcher<T>
    fun stop(watcher: Watcher<T>): Boolean
    fun stopAll()
}