package ru.mkn.krogue.model.mobs.strategy

import ru.mkn.krogue.model.game.Context
import ru.mkn.krogue.model.game.Unit
import ru.mkn.krogue.model.map.Position
import ru.mkn.krogue.model.mobs.behavior.ChasePlayer

internal class PlayerChaser(context: Context, unit: Unit) : MobStrategy(context, unit) {
    private val behavior = ChasePlayer()

    override fun doTurn(): Position {
        return behavior.doTurn(context, unit)
    }
}
