package viewmodel

import live.asFlow

fun <S> ViewModel<*, S>.asFlow() = ui.asFlow()