package ru.mkn.krogue.model.events

data class TimedGameEvent(val tick: Int, val event: GameEvent) : Comparable<TimedGameEvent> {
    override fun compareTo(other: TimedGameEvent): Int {
        return tick.compareTo(other.tick)
    }
}
