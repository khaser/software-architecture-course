package ru.mkn.krogue.model.events

data class TimedGameEvent(val tick: Int, val event: GameEvent)
