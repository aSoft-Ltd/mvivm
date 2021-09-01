package kotlinx.coroutines.universal

import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers

actual object Dispatchers {
    actual val Universal: CoroutineDispatcher = Dispatchers.Main
    actual val Main: MainCoroutineDispatcher = Dispatchers.Main
}