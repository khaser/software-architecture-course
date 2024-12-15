package ru.mkn.krogue.model.mobs.behavior

import ru.mkn.krogue.model.game.Context
import ru.mkn.krogue.model.game.Unit
import ru.mkn.krogue.model.map.Position

interface Behavior {
    fun doTurn(
        context: Context,
        unit: Unit,
    ): Position
}
