package ru.mkn.krogue.model.events

import ru.mkn.krogue.model.game.Context

sealed interface GameEvent {
    /**
     Returns delay before next execution
     */
    fun execute(context: Context): Int
}
