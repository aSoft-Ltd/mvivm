@file:JsExport

import live.Live
import react.*

fun <S> useLive(live: Live<S>): S {
    var state by useState(live.value)
    useEffectOnce {
        val listener = live.watch { state = it }
        cleanup { listener.stop() }
    }
    return state
}