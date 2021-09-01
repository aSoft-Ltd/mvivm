package viewmodel

import expect.BasicAssertion
import kotlin.test.assertTrue

class ViewModelExpectation<I, S, V : ViewModel<I, S>>(
    override val value: V
) : BasicAssertion<V>(value), ViewModelAssertion<S, V> {
    inline fun <reified E> toBeIn(): E {
        val state = value.ui.value
        assertTrue(
            """
            
        Expected State : ${E::class.simpleName}
        Actual State   : $state
        ==================================
        
        """.trimIndent()
        ) { state is E }
        return state as E
    }

    inline fun <reified L, reified R> toBeInEither(): Boolean {
        val state = value.ui.value
        assertTrue(
            """
    
    Expected States : [${L::class.simpleName} | ${R::class.simpleName}]
    Actual State    : $state
    =============================================================

    """.trimIndent()
        ) { state is L || state is R }
        return true
    }
}