package viewmodel

import useLive

fun <S> ViewModel<*,S>.asState() = useLive(ui)