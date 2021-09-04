@file:JsExport

import viewmodel.ViewModel

fun <S> useViewModelState(vm: ViewModel<*, S>): S = useLive(vm.ui)
