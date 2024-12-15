package ru.mkn.krogue.model.mobs.behavior

import ru.mkn.krogue.model.game.Context
import ru.mkn.krogue.model.game.Unit
import ru.mkn.krogue.model.map.Position
import ru.mkn.krogue.model.map.Tile

class Roam : Behavior {
    override fun doTurn(
        context: Context,
        unit: Unit,
    ): Position =
        context.run {
            unit.run {
                position.adjacentBySidePositions().filter { map.tiles[it] == Tile.FLOOR }.randomOrNull() ?: position
            }
        }
}
