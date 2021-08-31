@file:Suppress("PackageDirectoryMismatch")

import logging.TodoViewModel.Intent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import logging.*
import test.asyncTest
import java.util.concurrent.Executors
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class ViewModelLogging {

    init {
        Logging.init(ConsoleAppender())
    }

    val mainDispather = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(mainDispather)
    }

    @Test
    fun should_print_logging_output() = asyncTest {
        val vm = TodoViewModel()
        delay(50)
        vm.post(Intent.ReInit)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
        mainDispather.close()
    }
}