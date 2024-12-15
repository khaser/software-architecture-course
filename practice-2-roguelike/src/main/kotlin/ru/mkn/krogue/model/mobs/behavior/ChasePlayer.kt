package ru.mkn.krogue.model.mobs.behavior

import ru.mkn.krogue.model.Config
import ru.mkn.krogue.model.game.Context
import ru.mkn.krogue.model.game.Unit
import ru.mkn.krogue.model.map.Position
import ru.mkn.krogue.model.map.findPath

class ChasePlayer : Behavior {
    override fun doTurn(
        context: Context,
        unit: Unit,
    ): Position =
        context.run {
            val pathToPlayer = map.findPath(unit.position, player.position)
            val desiredPos =
                if (unit.position.distance(player.position) > Config.mobIntuitionDistance) {
                    Roam().doTurn(this, unit)
                } else {
                    pathToPlayer?.first() ?: Roam().doTurn(this, unit)
                }
            return if (isFreeFromMobs(desiredPos)) {
                desiredPos
            } else {
                unit.position
            }
        }
}
