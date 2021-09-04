package viewmodel

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlin.js.JsExport
import kotlin.jvm.JvmOverloads

@JsExport
open class IntentBus<I> @JvmOverloads constructor(
    val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
) {
    private val INTENT_BUS = MutableSharedFlow<I>(replay = 0)

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable -> mainDispatcherMissingHandler(throwable) }

    fun post(i: I) {
        coroutineScope.launch {
            INTENT_BUS.emit(i)
        }
    }

    private suspend fun collect(collector: suspend (I) -> Unit) {
        INTENT_BUS.collect(collector)
    }

    fun ViewModel<I, *>.observeIntentBus() = try {
        coroutineScope.launch(context = exceptionHandler) { collect { post(it) } }
    } catch (err: Throwable) {
        mainDispatcherMissingHandler(err)
    }
}

private fun mainDispatcherMissingHandler(throwable: Throwable) {
    val checkPhrase = "Module with the Main dispatcher is missing"
    if (throwable.message?.contains(checkPhrase) == true) {
        println(
            """
                $checkPhrase:
                This is okay if you are only testing your viewmodels
                This will blow up on production if you wont include a main dispather
                i.e. coroutines-android, coroutines-javafx, e.t.c 
            """.trimIndent()
        )
    } else throw throwable
}