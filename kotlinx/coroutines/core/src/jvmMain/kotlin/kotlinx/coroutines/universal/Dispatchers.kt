package kotlinx.coroutines.universal

import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers

actual object Dispatchers {
    actual val Universal: CoroutineDispatcher = Dispatchers.Default
    actual val Main: MainCoroutineDispatcher = Dispatchers.Main
}