@file:JvmName("ExpectBuilders")

package viewmodel

import kotlinx.atomic.collections.mutableAtomicListOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.universal.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.jvm.JvmName

fun <I, S, V : ViewModel<I, S>> expect(viewModel: V) = ViewModelExpectation(viewModel)

suspend fun <I, S> ViewModel<I, S>.expect(intent: I, with: CoroutineDispatcher = Dispatchers.Universal): ViewModelStateExpectation<S> {
    val states = mutableAtomicListOf<S>()
    val watcher = ui.watch { states.add(it) }
    withContext(with) { start(intent) }
    watcher.stop()
    return ViewModelStateExpectation(states.takeLast(states.size - 1))
}