package kotlinx.coroutines.universal

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

expect val Dispatchers.Test: CoroutineDispatcher
expect fun runTest(dispatcher: CoroutineDispatcher = Dispatchers.Test, block: suspend CoroutineScope.() -> Unit): Unit