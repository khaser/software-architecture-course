package ru.mkn.krogue.model.events

import ru.mkn.krogue.model.game.Context
import ru.mkn.krogue.model.game.Logger

sealed interface GameEvent {
    /**
     Returns delay before next execution
     */
    fun execute(
        context: Context,
        logger: Logger,
    ): Int
}
