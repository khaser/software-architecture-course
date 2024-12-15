package ru.mkn.krogue.model

import ru.mkn.krogue.model.events.MobTurn
import ru.mkn.krogue.model.events.PlayerHpRegen
import ru.mkn.krogue.model.events.TimedGameEvent
import ru.mkn.krogue.model.map.Direction
import ru.mkn.krogue.model.map.Tile
import ru.mkn.krogue.model.mobs.Mob
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
    private val mobTurnEvent = mutableMapOf<Mob, TimedGameEvent>()

    init {
        events.addAll(
            context.mobs.map {
                val event = TimedGameEvent(0, MobTurn(it))
                mobTurnEvent[it] = event
                event
            },
        )
        events.add(TimedGameEvent(0, PlayerHpRegen()))
    }

    private fun checkGameState() {
        if (state == GameState.OVER) {
            throw Exception("Trying to interact with game, that already over")
        }
    }

    private fun resumeToPlayerTurn(): GameState {
        checkGameState()

        val player = context.player
        val tickToStop = curTick + player.tempo

        while (events.isNotEmpty() && events.peek().tick < tickToStop) {
            val (tick, event) = events.poll()
            val nextFiringTick = tick + event.execute(context)
            val newEvent = TimedGameEvent(nextFiringTick, event)
            if (event is MobTurn) {
                mobTurnEvent[event.mob] = newEvent
            }
            events.add(newEvent)
        }
        curTick = tickToStop

        if (player.hp <= 0) {
            state = GameState.OVER
        }
        return state
    }

    fun movePlayer(dir: Direction): GameState =
        context.run {
            val newPos = player.position + dir
            return checkTileForUnits(newPos)?.let {
                fight(player, it)
                if (it.hp <= 0) {
                    mobs.remove(it)
                    events.remove(mobTurnEvent[it]!!)
                    mobTurnEvent.remove(it)
                }
                resumeToPlayerTurn()
            }
                ?: if (map.tiles[newPos] == Tile.FLOOR) {
                    player.position = newPos
                    resumeToPlayerTurn()
                } else if (map.tiles[newPos] == Tile.WALL) {
                    map.tiles[newPos] = Tile.FLOOR
                    resumeToPlayerTurn()
                } else {
                    GameState.IN_PROGRESS
                }
        }

    val playerEquipItem = { item: Item ->
        val oldItem = context.player.equipItem(item)
        Pair(oldItem, resumeToPlayerTurn())
    }

    val playerDropItem = { item: Item ->
        context.run {
            player.dropItem(item)
            map.items[player.position]?.add(item) ?: map.items.put(player.position, mutableListOf(item))
            resumeToPlayerTurn()
        }
    }

    val playerPickItem = {
        context.run {
            val items = map.items[player.position] ?: return@run GameState.IN_PROGRESS
            player.inventory.items.addAll(items)
            map.items.remove(player.position)
            resumeToPlayerTurn()
        }
    }
}
