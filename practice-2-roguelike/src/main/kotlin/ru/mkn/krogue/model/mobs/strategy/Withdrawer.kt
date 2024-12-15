package ru.mkn.krogue.model.mobs.strategy

import ru.mkn.krogue.model.game.Context
import ru.mkn.krogue.model.game.Unit
import ru.mkn.krogue.model.map.Position
import ru.mkn.krogue.model.mobs.behavior.Behavior
import ru.mkn.krogue.model.mobs.behavior.ChasePlayer
import ru.mkn.krogue.model.mobs.behavior.Flee

internal class Withdrawer(context: Context, unit: Unit) : MobStrategy(context, unit) {
    private var behavior: Behavior = ChasePlayer()

    override fun doTurn(): Position {
        return when (behavior) {
            is ChasePlayer -> {
                if (unit.hp >= unit.maxHp * 0.5) {
                    behavior.doTurn(context, unit)
                } else {
                    behavior = Flee()
                    behavior.doTurn(context, unit)
                }
            }
            is Flee -> {
                if (unit.hp >= unit.maxHp * 0.8) {
                    behavior = ChasePlayer()
                    behavior.doTurn(context, unit)
                } else {
                    behavior.doTurn(context, unit)
                }
            }
            else -> throw Exception("Illegal withdraw strategy state")
        }
    }
}
