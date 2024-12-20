package ru.mkn.krogue.model.game

import ru.mkn.krogue.model.Config
import ru.mkn.krogue.model.map.Map
import ru.mkn.krogue.model.map.Position
import ru.mkn.krogue.model.mobs.Mob
import ru.mkn.krogue.model.player.Equipment
import ru.mkn.krogue.model.player.Experience
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
            val playerPosition = map.getRandomFreePosition(mutableSetOf())
            val player =
                Player(
                    Unit(
                        playerPosition,
                        Config.Player.HP,
                        Config.Player.TEMPO,
                        Config.Player.REGEN_HP_CYCLE,
                        Config.Player.ATTACK,
                        Config.Player.DEFENCE,
                    ),
                    Inventory(
                        mutableListOf(),
                    ),
                    Equipment(),
                    Experience(),
                )
            val context = Context(player, map, mutableListOf())
            return context
        }
    }
}
