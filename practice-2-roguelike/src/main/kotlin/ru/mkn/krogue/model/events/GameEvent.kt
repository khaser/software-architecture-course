package ru.mkn.krogue.model.events

import ru.mkn.krogue.model.GameContext

interface GameEvent {
    /**
     Returns delay before next execution
     */
    fun execute(context: GameContext): Int
}
