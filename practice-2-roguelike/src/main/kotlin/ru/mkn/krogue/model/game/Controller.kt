package ru.mkn.krogue.model.game

import ru.mkn.krogue.model.Config
import ru.mkn.krogue.model.Item
import ru.mkn.krogue.model.events.MobTurn
import ru.mkn.krogue.model.events.TimedGameEvent
import ru.mkn.krogue.model.map.Direction
import ru.mkn.krogue.model.map.Tile
import ru.mkn.krogue.model.mobs.Mob
import ru.mkn.krogue.model.player.LevelUPStat
import java.util.PriorityQueue

class Controller(
    val context: Context = Context.newFromConfig(),
    val logger: Logger = Logger(),
) {
    private var state = State.IN_PROGRESS
    private var curTick = 0
    private val events = PriorityQueue<TimedGameEvent>()
    private val mobTurnEvent = mutableMapOf<Mob, TimedGameEvent>()

    init {
        val fantasyMobFactory = Config.Mobs.factory(context, logger, ::register)
        context.run {
            val occupiedPositions = mutableSetOf(player.position)
            (0 until Config.MOB_COUNT).map {
                val mobPosition = map.getRandomFreePosition(occupiedPositions)
                register(fantasyMobFactory.newRandomMob(mobPosition))
                occupiedPositions.add(mobPosition)
            }
        }
    }

    private fun register(mob: Mob) {
        context.run {
            val event = TimedGameEvent(curTick + mob.tempo, MobTurn(mob))
            mobTurnEvent[mob] = event
            events.add(event)
            mobs.add(mob)
            Unit
        }
    }

    private fun checkGameState() {
        if (state == State.OVER) {
            throw Exception("Trying to interact with game, that already over")
        }
    }

    private fun resumeToPlayerTurn(): State {
        checkGameState()
        val player = context.player
        player.regenerateHp()

        val tickToStop = curTick + player.tempo

        while (events.isNotEmpty() && events.peek().tick < tickToStop) {
            val (tick, event) = events.poll()
            val nextFiringTick = tick + event.execute(context, logger)
            val newEvent = TimedGameEvent(nextFiringTick, event)
            if (event is MobTurn) {
                mobTurnEvent[event.mob] = newEvent
            }
            events.add(newEvent)
        }
        curTick = tickToStop

        if (player.hp <= 0) {
            state = State.OVER
        }
        return state
    }

    private fun killMobIfNeeded(it: Mob) {
        if (it.hp <= 0) {
            context.mobs.remove(it)
            logger.log("${it.appearance} was killed!")
            events.remove(mobTurnEvent[it]!!)
            mobTurnEvent.remove(it)
            context.player.experience.getPoints(it.xp)
        }
    }

    val playerMoveTo = { dir: Direction ->
        context.run {
            val newPos = player.position + dir
            getMobIn(newPos)?.let {
                player.dealDamage(it)
                logger.log("Player attacks ${it.appearance}.")
                killMobIfNeeded(it)
                resumeToPlayerTurn()
            }
                ?: if (map.tiles[newPos] == Tile.FLOOR) {
                    player.position = newPos
                    resumeToPlayerTurn()
                } else if (map.tiles[newPos] == Tile.WALL) {
                    map.tiles[newPos] = Tile.FLOOR
                    resumeToPlayerTurn()
                } else {
                    State.IN_PROGRESS
                }
        }
    }

    val playerEquipItem = { item: Item ->
        val oldItem = context.player.equipItem(item)
        logger.log("$item was equipped.")
        Pair(oldItem, resumeToPlayerTurn())
    }

    val playerDropItem = { item: Item ->
        context.run {
            player.dropItem(item)
            logger.log("Item $item was dropped to the floor.")
            map.items[player.position]?.add(item) ?: map.items.put(player.position, mutableListOf(item))
            resumeToPlayerTurn()
        }
    }

    val playerPickItem = {
        context.run {
            val items = map.items[player.position] ?: return@run State.IN_PROGRESS
            logger.log("Items $items was picked up.")
            player.inventory.items.addAll(items)
            map.items.remove(player.position)
            resumeToPlayerTurn()
        }
    }

    val playerSkip = {
        resumeToPlayerTurn()
    }

    val playerGainLevel = { choice: LevelUPStat ->
        context.run {
            if (player.experience.levelPoints <= 0) {
                throw IllegalCallerException("We have no level points to use!")
            }
            player.experience.levelPoints -= 1
            when (choice) {
                LevelUPStat.HP -> {
                    player.maxHp += 5
                    logger.log("Player looks healthier.")
                }
                LevelUPStat.ATK -> {
                    player.baseAttack += 1
                    logger.log("Player looks stronger.")
                }
                LevelUPStat.DEF -> {
                    player.baseDefense += 1
                    logger.log("Player looks tougher.")
                }
            }
        }
    }
}
