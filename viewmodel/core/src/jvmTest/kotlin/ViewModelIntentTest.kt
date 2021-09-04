import CounterViewModel.Intent
import CounterViewModel.State
import expect.expect
import kotlinx.coroutines.delay
import kotlinx.coroutines.runTest
import logging.ConsoleAppender
import logging.Logging
import viewmodel.ViewModel
import viewmodel.expect
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Give a [ViewModel] [CounterViewModel]
 */
class ViewModelIntentTest {

    init {
        Logging.init(ConsoleAppender())
    }

    @Test
    fun state_should_follow_intents() = runTest {
        val vm = CounterViewModel()
        expect(vm.countState).toBe(0)
        delay(10)
        vm.expect(Intent.CountUp(1)).toGoThrough(State(1))

        vm.expect(Intent.CountUp(2)).toGoThrough(State(3))
        delay(10)
        assertEquals(3, vm.countState)
    }
}