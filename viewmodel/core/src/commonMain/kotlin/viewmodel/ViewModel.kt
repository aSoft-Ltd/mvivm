package viewmodel

import kotlinx.coroutines.*
import kotlinx.coroutines.universal.Dispatchers
import live.Live
import logging.logger
import kotlin.js.JsExport
import kotlin.jvm.JvmOverloads

@JsExport
abstract class ViewModel<in I, S> @JvmOverloads constructor(
    initialState: S,
    scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
) : PlatformViewModel() {
    internal val logger = logger(this::class.simpleName ?: "Anonymous ViewModel")
    val ui : Live<S> = Live(initialState)
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