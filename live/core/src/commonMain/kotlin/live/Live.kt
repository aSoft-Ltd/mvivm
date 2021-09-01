package live

expect class Live<S>(state: S) {
    var value: S
    fun watch(callable: (state: S) -> Unit): Watcher<S>
    fun stop(watcher: Watcher<S>): Boolean
    fun stopAll()
}