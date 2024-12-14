package ru.mkn.krogue.model

import ru.mkn.krogue.model.events.MobTurn
import ru.mkn.krogue.model.events.TimedGameEvent
import ru.mkn.krogue.model.map.Direction
import ru.mkn.krogue.model.map.Tile
import java.util.PriorityQueue

enum class GameState {
    IN_PROGRESS,
    OVER,
}

class GameController(
    val context: GameContext,
) {
    private var state = GameState.IN_PROGRESS
    private var curTick = 0
    private val events = PriorityQueue<TimedGameEvent>()

    init {
        context.mobs.forEach { mob ->
            val turnEvent = MobTurn(mob)
            events.add(TimedGameEvent(0, turnEvent))
        }
    }

    private fun checkGameState() {
        if (state == GameState.OVER) {
            throw Exception("Trying to interact with game, that already over")
        }
    }

    private fun resumeToPlayerTurn(): GameState {
        checkGameState()

        val player = context.player
        val tickToStop = curTick + player.unit.tempo

        while (events.isNotEmpty() && events.peek().tick < tickToStop) {
            val (tick, event) = events.poll()
            val nextFiringTick = tick + event.execute(context)
            events.add(TimedGameEvent(nextFiringTick, event))
        }
        curTick = tickToStop

        if (player.hp <= 0) {
            state = GameState.OVER
        }
        return state
    }

    fun movePlayer(dir: Direction): GameState {
        context.run {
            val newPos = player.position + dir
            if (map.tiles[newPos] == Tile.FLOOR) {
                player.position = newPos
            }
        }
        return resumeToPlayerTurn()
    }
}
