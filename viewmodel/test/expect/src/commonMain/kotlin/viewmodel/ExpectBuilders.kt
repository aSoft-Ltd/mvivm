@file:JvmName("ExpectBuilders")

package viewmodel

import kotlinx.atomic.collections.mutableAtomicListOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.universal.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.jvm.JvmName

fun <I, S, V : ViewModel<I, S>> expect(viewModel: V) = ViewModelExpectation(viewModel)

suspend fun <I, S> ViewModel<I, S>.expectIn(dispatcher: CoroutineDispatcher, intent: I): ViewModelStateExpectation<S> {
    val states = mutableAtomicListOf<S>()
    val watcher = ui.watch { states.add(it) }
    withContext(dispatcher) {
        start(intent)
    }
    watcher.stop()
    return ViewModelStateExpectation(states.takeLast(states.size - 1))
}

suspend fun <I, S> ViewModel<I, S>.expect(intent: I) = expectIn(Dispatchers.Universal, intent)