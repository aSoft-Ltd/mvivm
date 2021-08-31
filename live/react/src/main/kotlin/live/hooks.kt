package live

import react.*

@JsExport
@JsName("useLive")
fun <S> useLive(live: Live<S>): S {
    val (state, setState) = useState(live.value)
    rawUseEffect({
        val listener = live.watch { setState(it) }
        val cleanup: () -> Unit = { listener.stop() }
        cleanup
    }, emptyArray())
    return state
}