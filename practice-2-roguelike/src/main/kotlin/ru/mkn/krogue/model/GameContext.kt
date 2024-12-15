package ru.mkn.krogue.model

import ru.mkn.krogue.model.map.Map
import ru.mkn.krogue.model.map.Position
import ru.mkn.krogue.model.mobs.Mob
import ru.mkn.krogue.model.mobs.MobAppearance
import ru.mkn.krogue.model.player.Equipment
import ru.mkn.krogue.model.player.Inventory
import ru.mkn.krogue.model.player.Player

data class GameContext(
    val player: Player,
    val map: Map,
    val mobs: MutableList<Mob>,
) {
    companion object {
        fun newFromConfig(): GameContext {
            val map = Map.generate(Config.mapSize)
            val occupiedPositions: MutableSet<Position> = mutableSetOf()
            val playerPosition = map.getRandomFreePosition(occupiedPositions)
            val player =
                Player(
                    GameUnit(playerPosition, Config.Player.hp, Config.Player.temper),
                    Inventory(
                        mutableListOf(),
                    ),
                    Equipment(),
                )
            val context = GameContext(player, map, mutableListOf())
            val mobs =
                (0 until Config.mobCount).map {
                    val mobPosition = map.getRandomFreePosition(occupiedPositions)
                    Mob.new(MobAppearance.ZOMBIE, context, mobPosition)
                }
            context.mobs.addAll(mobs)
            return context
        }
    }
}
