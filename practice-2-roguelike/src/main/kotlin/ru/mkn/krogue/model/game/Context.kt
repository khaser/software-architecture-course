package ru.mkn.krogue.model.game

import ru.mkn.krogue.model.Config
import ru.mkn.krogue.model.map.Map
import ru.mkn.krogue.model.map.Position
import ru.mkn.krogue.model.mobs.Mob
import ru.mkn.krogue.model.mobs.MobAppearance
import ru.mkn.krogue.model.player.Equipment
import ru.mkn.krogue.model.player.Inventory
import ru.mkn.krogue.model.player.Player

data class Context(
    val player: Player,
    val map: Map,
    val mobs: MutableList<Mob>,
) {
    fun getUnitIn(pos: Position): Unit? {
        if (player.position == pos) return player
        return getMobIn(pos)
    }

    fun getMobIn(pos: Position): Mob? {
        return mobs.find { it.position == pos }
    }

    fun isFree(pos: Position) = getUnitIn(pos) == null

    fun isFreeFromMobs(pos: Position): Boolean {
        return when (getUnitIn(pos)) {
            null -> true
            is Player -> return true
            else -> return false
        }
    }

    companion object {
        fun newFromConfig(): Context {
            val map = Map.generate(Config.mapSize)
            val occupiedPositions: MutableSet<Position> = mutableSetOf()
            val playerPosition = map.getRandomFreePosition(occupiedPositions)
            val player =
                Player(
                    playerPosition,
                    Config.Player.hp,
                    Config.Player.temper,
                    Inventory(
                        mutableListOf(),
                    ),
                    Equipment(),
                )
            val context = Context(player, map, mutableListOf())
            val mobs =
                (0 until Config.mobCount).map {
                    val mobPosition = map.getRandomFreePosition(occupiedPositions)
                    Mob.new(MobAppearance.entries.shuffled().first(), context, mobPosition)
                }
            context.mobs.addAll(mobs)
            return context
        }
    }
}
