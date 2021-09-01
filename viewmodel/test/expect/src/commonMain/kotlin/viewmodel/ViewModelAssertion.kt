package viewmodel

import expect.BasicExpectation
import kotlin.test.assertEquals

interface ViewModelAssertion<S, V : ViewModel<*, S>> : BasicExpectation<V> {
    fun toBeIn(expectedState: S) = assertEquals(expectedState, value.ui.value)
}