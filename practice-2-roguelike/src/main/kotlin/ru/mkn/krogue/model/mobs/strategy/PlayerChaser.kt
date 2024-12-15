package ru.mkn.krogue.model.mobs.strategy

import ru.mkn.krogue.model.GameContext
import ru.mkn.krogue.model.GameUnit
import ru.mkn.krogue.model.map.Position
import ru.mkn.krogue.model.mobs.behavior.ChasePlayer

internal class PlayerChaser(context: GameContext, unit: GameUnit) : MobStrategy(context, unit) {
    private val behavior = ChasePlayer()

    override fun doTurn(): Position {
        return behavior.doTurn(context, unit)
    }
}
