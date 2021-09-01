@file:JsExport

import react.useMemo
import viewmodel.ViewModel

fun <V : ViewModel<*, *>> useViewModel(builder: () -> V): V = useMemo(builder, arrayOf())

fun <S> useViewModelState(vm: ViewModel<*, S>): S = useLive(vm.ui)

fun <V : ViewModel<*, *>> viewModel(builder: () -> V): V = useMemo(builder, arrayOf())
