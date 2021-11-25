package viewmodel

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import react.*
import react.Component as ReactComponent
import kotlin.coroutines.CoroutineContext

abstract class Component<P : Props, S : State> : ReactComponent<P, S>, CoroutineScope {
    constructor() : super()
    constructor(props: P) : super(props)

    protected val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    protected fun syncState(
        context: CoroutineContext = coroutineContext,
        buildState: suspend S.() -> Unit
    ) {
        launch(context) {
            state.buildState()
            setState { }
        }
    }

    fun <T> Flow<T>.observe(
        context: CoroutineContext = coroutineContext,
        onChange: suspend (T) -> Unit
    ) = launch(context) { collect { onChange(it) } }

    override fun componentWillUnmount() {
        cancel()
    }
    
    abstract fun RBuilder.render(): dynamic

    override fun render() = createElement { render() }
}