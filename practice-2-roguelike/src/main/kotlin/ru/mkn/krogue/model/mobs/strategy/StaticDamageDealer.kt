package ru.mkn.krogue.model.mobs.strategy

import ru.mkn.krogue.model.GameContext
import ru.mkn.krogue.model.GameUnit
import ru.mkn.krogue.model.map.Position

internal class StaticDamageDealer(context: GameContext, unit: GameUnit) : MobStrategy(context, unit) {
    override fun doTurn(): Position {
        val playerPos = context.player.position
        val mobPos = unit.position
        return if (mobPos.distance(playerPos) == 1) {
            playerPos
        } else {
            mobPos
        }
    }
}
