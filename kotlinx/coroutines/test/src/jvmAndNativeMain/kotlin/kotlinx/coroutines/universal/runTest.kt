package kotlinx.coroutines.universal

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

private val TestDispatcher by lazy { kotlinx.coroutines.Dispatchers.Default }
actual val Dispatchers.Test get() = TestDispatcher
actual fun runTest(dispatcher: CoroutineDispatcher, block: suspend CoroutineScope.() -> Unit): Unit = runBlocking {
    withContext(dispatcher) { block() }
}