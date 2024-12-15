package ru.mkn.krogue.model.mobs.behavior

import ru.mkn.krogue.model.GameContext
import ru.mkn.krogue.model.GameUnit
import ru.mkn.krogue.model.map.Position

interface Behavior {
    fun doTurn(
        context: GameContext,
        unit: GameUnit,
    ): Position
}
