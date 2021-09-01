package kotlinx.coroutines.universal

import kotlinx.coroutines.CompletionHandler

internal abstract class CancelHandlerBase {
    @JsName("invoke")
    abstract fun invoke(cause: Throwable?)
}

internal abstract class CancelHandler public constructor() : CancelHandlerBase()

@Suppress("UnsafeCastFromDynamic")
internal inline val CancelHandlerBase.asHandler: CompletionHandler
    get() = asDynamic()