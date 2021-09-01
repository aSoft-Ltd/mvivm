package kotlinx.coroutines.universal

import kotlinx.browser.window
import kotlinx.coroutines.*
import org.w3c.dom.Window
import kotlin.coroutines.CoroutineContext
import kotlin.js.Promise

private external val navigator: dynamic
private const val UNDEFINED = "undefined"
private external val process: dynamic

actual object Dispatchers {
    actual val Universal: CoroutineDispatcher = when {
        // Check if we are running under ReactNative. We have to use NodeDispatcher<BUG HERE> under it.
        // The problem is that ReactNative has a `window` object with `addEventListener`, but it does not  really work.
        // For details see https://github.com/Kotlin/kotlinx.coroutines/issues/236
        // The check for ReactNative is based on https://github.com/facebook/react-native/commit/3c65e62183ce05893be0822da217cb803b121c61
        jsTypeOf(navigator) != UNDEFINED && navigator != null && navigator.product == "ReactNative" -> SetTimeoutDispatcher
        // Check if we are in the browser and must use window.postMessage to avoid setTimeout throttling
        jsTypeOf(window) != "undefined" && jsTypeOf(window.asDynamic().addEventListener) != "undefined" -> window.asCoroutineDispatcher()
        jsTypeOf(window) != UNDEFINED && window.asDynamic() != null && jsTypeOf(window.asDynamic().addEventListener) != UNDEFINED -> window.asCoroutineDispatcher()
        // Fallback to NodeDispatcher when browser environment is not detected
        else -> NodeDispatcher
    }
    actual val Main: MainCoroutineDispatcher = JsMainDispatcher(Universal, false)
}

private class JsMainDispatcher(
    val delegate: CoroutineDispatcher,
    private val invokeImmediately: Boolean
) : MainCoroutineDispatcher() {
    override val immediate: MainCoroutineDispatcher =
        if (invokeImmediately) this else JsMainDispatcher(delegate, true)

    override fun isDispatchNeeded(context: CoroutineContext): Boolean = !invokeImmediately
    override fun dispatch(context: CoroutineContext, block: Runnable) = delegate.dispatch(context, block)

    @OptIn(InternalCoroutinesApi::class)
    override fun dispatchYield(context: CoroutineContext, block: Runnable) = delegate.dispatchYield(context, block)

    protected fun toStringInternalCustomImpl(): String? {
        val main = kotlinx.coroutines.universal.Dispatchers.Main
        if (this === main) return "Dispatchers.Main"
        val immediate =
            try {
                main.immediate
            } catch (e: UnsupportedOperationException) {
                null
            }
        if (this === immediate) return "Dispatchers.Main.immediate"
        return null
    }

    override fun toString(): String = toStringInternalCustomImpl() ?: delegate.toString()
}

private const val MAX_DELAY = Int.MAX_VALUE.toLong()

private fun delayToInt(timeMillis: Long): Int =
    timeMillis.coerceIn(0, MAX_DELAY).toInt()

@OptIn(InternalCoroutinesApi::class)
internal sealed class SetTimeoutBasedDispatcher : CoroutineDispatcher(), Delay {
    inner class ScheduledMessageQueue : MessageQueue() {
        internal val processQueue: dynamic = { process() }

        override fun schedule() {
            scheduleQueueProcessing()
        }

        override fun reschedule() {
            setTimeout(processQueue, 0)
        }
    }

    internal val messageQueue = ScheduledMessageQueue()

    abstract fun scheduleQueueProcessing()

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        messageQueue.enqueue(block)
    }

    override fun invokeOnTimeout(timeMillis: Long, block: Runnable, context: CoroutineContext): DisposableHandle {
        val handle = setTimeout({ block.run() }, delayToInt(timeMillis))
        return ClearTimeout(handle)
    }

    override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
        val handle = setTimeout({ with(continuation) { resumeUndispatched(Unit) } }, delayToInt(timeMillis))
        // Actually on cancellation, but clearTimeout is idempotent
        continuation.invokeOnCancellation(handler = ClearTimeout(handle).asHandler)
    }
}

internal object SetTimeoutDispatcher : SetTimeoutBasedDispatcher() {
    override fun scheduleQueueProcessing() {
        setTimeout(messageQueue.processQueue, 0)
    }
}

internal object NodeDispatcher : SetTimeoutBasedDispatcher() {
    override fun scheduleQueueProcessing() {
        process.nextTick(messageQueue.processQueue)
    }
}

private class ClearTimeout(private val handle: Int) : CancelHandler(), DisposableHandle {

    override fun dispose() {
        clearTimeout(handle)
    }

    override fun invoke(cause: Throwable?) {
        dispose()
    }

    override fun toString(): String = "ClearTimeout[$handle]"
}

@OptIn(InternalCoroutinesApi::class)
internal class WindowDispatcher(private val window: Window) : CoroutineDispatcher(), Delay {
    private val queue = WindowMessageQueue(window)

    override fun dispatch(context: CoroutineContext, block: Runnable) = queue.enqueue(block)

    override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
        window.setTimeout({ with(continuation) { resumeUndispatched(Unit) } }, delayToInt(timeMillis))
    }

    override fun invokeOnTimeout(timeMillis: Long, block: Runnable, context: CoroutineContext): DisposableHandle {
        val handle = window.setTimeout({ block.run() }, delayToInt(timeMillis))
        return object : DisposableHandle {
            override fun dispose() {
                window.clearTimeout(handle)
            }
        }
    }
}

private class WindowMessageQueue(private val window: Window) : MessageQueue() {
    private val messageName = "dispatchCoroutine"

    init {
        window.addEventListener("message", { event: dynamic ->
            if (event.source == window && event.data == messageName) {
                event.stopPropagation()
                process()
            }
        }, true)
    }

    override fun schedule() {
        Promise.resolve(Unit).then({ process() })
    }

    override fun reschedule() {
        window.postMessage(messageName, "*")
    }
}

/**
 * An abstraction over JS scheduling mechanism that leverages micro-batching of dispatched blocks without
 * paying the cost of JS callbacks scheduling on every dispatch.
 *
 * Queue uses two scheduling mechanisms:
 * 1) [schedule] is used to schedule the initial processing of the message queue.
 *    JS engine-specific microtask mechanism is used in order to boost performance on short runs and a dispatch batch
 * 2) [reschedule] is used to schedule processing of the queue after yield to the JS event loop.
 *    JS engine-specific macrotask mechanism is used not to starve animations and non-coroutines macrotasks.
 *
 * Yet there could be a long tail of "slow" reschedules, but it should be amortized by the queue size.
 */
internal abstract class MessageQueue : ArrayList<Runnable>() {
    val yieldEvery = 16 // yield to JS macrotask event loop after this many processed messages
    private var scheduled = false

    abstract fun schedule()

    abstract fun reschedule()

    fun enqueue(element: Runnable) {
        add(element)
        if (!scheduled) {
            scheduled = true
            schedule()
        }
    }

    fun process() {
        try {
            // limit number of processed messages
            repeat(yieldEvery) {
                val element = removeFirstOrNull() ?: return@process
                element.run()
            }
        } finally {
            if (isEmpty()) {
                scheduled = false
            } else {
                reschedule()
            }
        }
    }
}

// We need to reference global setTimeout and clearTimeout so that it works on Node.JS as opposed to
// using them via "window" (which only works in browser)
private external fun setTimeout(handler: dynamic, timeout: Int = definedExternally): Int
private external fun clearTimeout(handle: Int = definedExternally)