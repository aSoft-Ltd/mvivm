package live

import kotlin.js.JsExport
import kotlin.js.JsName

@JsExport
class Watcher<T> internal constructor(
    internal val callable: (T) -> Unit,
    private val container: MutableList<Watcher<T>>
) {
    @JsName("stop")
    fun stop() = container.remove(this)
}