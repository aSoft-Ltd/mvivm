package viewmodel

import live.Watcher
import react.RBuilder
import react.RProps
import react.RState
import react.setState
import reakt.Component
import viewmodel.VComponent.UIState

abstract class VComponent<P : RProps, I, S, V : ViewModel<I, S>> : Component<P, UIState<S>> {
    abstract val viewModel: V
    protected var watcher: Watcher<S>? = null

    class UIState<S>(var ui: S?) : RState

    constructor() : super()

    constructor(props: P) : super(props)

    init {
        state = UIState(null)
    }

    @JsName("build")
    abstract fun RBuilder.render(ui: S): dynamic

    override fun RBuilder.render() {
        state.ui?.let { render(it) }
    }

    override fun componentDidMount() {
        watcher = viewModel.ui.watch {
            setState(UIState(it))
        }
    }

    override fun componentWillUnmount() {
        watcher?.stop()
        watcher = null
        super.componentWillUnmount()
    }

    inline fun post(i: I) = viewModel.post(i)
}