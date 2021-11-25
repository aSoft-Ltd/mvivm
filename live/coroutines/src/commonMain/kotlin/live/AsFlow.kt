package live

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun <S> Live<S>.asFlow(): Flow<S> = callbackFlow {
    val watcher = watch { trySend(it) }
    awaitClose { watcher.stop() }
}