package ru.mkn.krogue.model.mobs.strategy

import ru.mkn.krogue.model.GameContext
import ru.mkn.krogue.model.GameUnit
import ru.mkn.krogue.model.map.Position
import ru.mkn.krogue.model.mobs.behavior.Roam

internal class PeacefulInhabitant(context: GameContext, unit: GameUnit) : MobStrategy(context, unit) {
    private val behavior = Roam()

    override fun doTurn(): Position {
        return behavior.doTurn(context, unit)
    }
}
