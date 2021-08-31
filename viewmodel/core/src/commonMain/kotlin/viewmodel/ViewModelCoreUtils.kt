@file:JvmMultifileClass

package viewmodel

import kotlin.jvm.JvmMultifileClass

internal val <T : Any> T.toDetailedString: String
    get() = when {
        "$this" == "[object Object]" -> this::class.simpleName ?: "Unknown"
        "$this".contains("${'$'}State${'$'}") -> this::class.simpleName ?: "Unknown"
        "$this".contains("${'$'}Intent${'$'}") -> this::class.simpleName ?: "Unknown"
        else -> toString()
    }

fun <I> ViewModel<I, *>.log(intent: I) = log("Sending Intent ${intent?.toDetailedString}")

fun ViewModel<*, *>.log(msg: String) = when {
    msg.contains("error", ignoreCase = true) -> logger.error(msg)
    msg.contains("fail", ignoreCase = true) -> logger.failure(msg)
    else -> logger.info(msg)
}