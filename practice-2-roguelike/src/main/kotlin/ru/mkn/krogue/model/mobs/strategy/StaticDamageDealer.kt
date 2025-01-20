package ru.mkn.krogue.model.mobs.strategy

import ru.mkn.krogue.model.game.Context
import ru.mkn.krogue.model.game.Unit
import ru.mkn.krogue.model.map.Position

internal class StaticDamageDealer(context: Context, unit: Unit) : MobStrategy(context, unit) {
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
