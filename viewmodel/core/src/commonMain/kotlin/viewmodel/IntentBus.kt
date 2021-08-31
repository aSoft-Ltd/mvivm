package viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

open class IntentBus<I>(val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob())) {
    private val INTENT_BUS = MutableSharedFlow<I>(replay = 0)

    fun post(i: I) {
        coroutineScope.launch {
            INTENT_BUS.emit(i)
        }
    }

    open suspend fun collect(collector: suspend (I) -> Unit) {
        INTENT_BUS.collect(collector)
    }

    fun ViewModel<I, *>.observeIntentBus() = coroutineScope.launch { collect { post(it) } }
}