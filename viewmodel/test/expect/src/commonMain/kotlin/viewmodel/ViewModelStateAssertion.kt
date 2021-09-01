package viewmodel

import expect.BasicExpectation
import kotlin.test.assertEquals

interface ViewModelStateAssertion<S> : BasicExpectation<List<S>> {

    fun toBeIn(state: S) {
        assertEquals(
            state, value.lastOrNull(),
            """
            
        Expected ViewModel State : $state
        Actual ViewModel State   : ${value.lastOrNull()}
        ==================================
        
        """.trimIndent()
        )
    }

    fun toGoThrough(vararg states: S) {
        assertEquals(
            states.toList(), value,
            """
            
        Expected ViewModel State Path : ${states.toList()}
        Actual ViewModel States Path  : $value
        ==================================
        
        """.trimIndent()
        )
    }
}