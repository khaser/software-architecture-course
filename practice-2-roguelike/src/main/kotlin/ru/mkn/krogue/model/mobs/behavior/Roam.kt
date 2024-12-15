package ru.mkn.krogue.model.mobs.behavior

import ru.mkn.krogue.model.GameContext
import ru.mkn.krogue.model.GameUnit
import ru.mkn.krogue.model.map.Position
import ru.mkn.krogue.model.map.Tile

class Roam : Behavior {
    override fun doTurn(
        context: GameContext,
        unit: GameUnit,
    ): Position =
        context.run {
            unit.run {
                position.adjacentBySidePositions().filter { map.tiles[it] == Tile.FLOOR }.randomOrNull() ?: position
            }
        }
}
