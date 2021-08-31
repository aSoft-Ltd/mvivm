import live.Live
import kotlin.test.Test

class LiveTest {
    @Test
    fun should_have_a_valid_syntax() {
        val liveInt = Live(1)
        val watcher1 = liveInt.watch {
            println("Watcher 1: $it")
        }
        liveInt.value = 2
        liveInt.value = 3
        val watcher2 = liveInt.watch {
            println("Watcher 2: $it")
        }
        liveInt.value = 4
        liveInt.value = 5
//        liveInt.stop(watcher1)
        watcher1.stop()
        liveInt.value = 6
        liveInt.value = 7
//        liveInt.stop(watcher2)
        watcher2.stop()
        liveInt.value = 8
        watcher2.stop()
        liveInt.value = 9
        liveInt.value = 10
    }
}