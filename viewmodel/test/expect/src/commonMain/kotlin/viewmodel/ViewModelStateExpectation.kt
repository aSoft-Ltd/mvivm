package viewmodel

import expect.BasicAssertion
import kotlin.test.assertTrue

class ViewModelStateExpectation<S>(
    override val value: List<S>
) : BasicAssertion<List<S>>(value), ViewModelStateAssertion<S> {
    inline fun <reified E> toBeIn(): E {
        val state = value.last()
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
        val state = value.last()
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