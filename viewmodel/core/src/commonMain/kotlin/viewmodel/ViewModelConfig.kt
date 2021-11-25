package viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import logging.ConsoleAppender
import logging.Logger
import kotlin.jvm.JvmField
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic
import kotlin.jvm.JvmSynthetic

interface ViewModelConfig {
    val scopeBuilder: () -> CoroutineScope
    val logger: Logger?

    companion object {
        @JvmField
        val DEFAULT_LOGGER = Logger(ConsoleAppender())

        @JvmField
        val DEFAULT_SCOPE = CoroutineScope(SupervisorJob() + Dispatchers.Main)

        @JvmField
        val DEFAULT_SCOPE_BUILDER = { DEFAULT_SCOPE }

        @JvmSynthetic
        operator fun invoke(
            logger: Logger = DEFAULT_LOGGER,
            builder: () -> CoroutineScope = DEFAULT_SCOPE_BUILDER
        ) = object : ViewModelConfig {
            override val logger: Logger = logger
            override val scopeBuilder: () -> CoroutineScope = builder
        }

        @JvmOverloads
        @JvmStatic
        fun create(
            logger: Logger = DEFAULT_LOGGER,
            builder: () -> CoroutineScope = DEFAULT_SCOPE_BUILDER
        ) = invoke(logger, builder)
    }
}