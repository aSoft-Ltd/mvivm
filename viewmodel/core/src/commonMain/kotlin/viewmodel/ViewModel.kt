package viewmodel

import kotlinx.coroutines.*
import live.Live
import logging.logger

abstract class ViewModel<in I, S>(initialState: S, scope: CoroutineScope = MainScope()) : PlatformViewModel() {
    internal val logger = logger(this::class.simpleName ?: "Anonymous ViewModel")
    val ui = Live(initialState)
    open val coroutineScope = scope

    init {
        ui.watch { log("State at ${it?.toDetailedString}") }
    }

    /**
     * Will execute the provided intent on the viewmodel's scope
     */
    open fun post(i: I) {
        log(i)
        coroutineScope.execute(i)
    }

    /**
     * Will execute the provided intent on the calling scope
     */
    open fun CoroutineScope.start(i: I) {
        log(i)
        execute(i)
    }

    abstract fun CoroutineScope.execute(i: I): Any

    override fun onCleared() {
        ui.stopAll()
        coroutineScope.cancel()
    }
}