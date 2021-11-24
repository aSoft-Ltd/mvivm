package viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import live.Live
import logging.Logger
import logging.logger
import kotlin.js.JsExport
import kotlin.jvm.JvmOverloads

@JsExport
abstract class ViewModel<in I, S> @JvmOverloads constructor(
    initialState: S,
    config: ViewModelConfig = ViewModelConfig()
) : PlatformViewModel() {
    val logger = config.logger
    val ui: Live<S> = Live(initialState)
    open val coroutineScope by lazy(config.scopeBuilder)

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

    protected abstract fun CoroutineScope.execute(i: I): Any

    override fun onCleared() {
        ui.stopAll()
        coroutineScope.cancel()
    }
}