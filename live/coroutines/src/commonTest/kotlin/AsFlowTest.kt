import expect.expect
import expect.toBe
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import live.Live
import live.asFlow
import kotlin.test.Test

class AsFlowTest {
    @Test
    fun should_wait_for_values() = runTest {
        val live = Live(-1)
        val scope = CoroutineScope(SupervisorJob())
        launch {
            repeat(4) {
                live.value = it
                delay(1000)
            }
            scope.cancel()
        }
        try {
            withContext(scope.coroutineContext) {
                live.asFlow().collect {
                    println(it)
                }
            }
        } catch (err: Throwable) {
            expect(err).toBe<CancellationException>()
        }
    }
}