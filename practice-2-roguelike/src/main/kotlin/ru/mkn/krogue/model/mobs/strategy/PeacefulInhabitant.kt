package ru.mkn.krogue.model.mobs.strategy

import ru.mkn.krogue.model.game.Context
import ru.mkn.krogue.model.game.Unit
import ru.mkn.krogue.model.map.Position
import ru.mkn.krogue.model.mobs.behavior.Roam

internal class PeacefulInhabitant(context: Context, unit: Unit) : MobStrategy(context, unit) {
    private val behavior = Roam()

    override fun doTurn(): Position {
        return behavior.doTurn(context, unit)
    }
}
