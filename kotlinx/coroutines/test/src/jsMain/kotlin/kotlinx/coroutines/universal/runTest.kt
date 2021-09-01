package kotlinx.coroutines.universal

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise

private val TestDispatcher by lazy { Dispatchers.Universal }
actual val Dispatchers.Test get() = TestDispatcher
actual fun runTest(dispatcher: CoroutineDispatcher, block: suspend CoroutineScope.() -> Unit): Unit = GlobalScope.promise(block = block).unsafeCast<dynamic>()