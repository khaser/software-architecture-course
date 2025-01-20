package ru.mkn.krogue.model.mobs.behavior

import ru.mkn.krogue.model.game.Context
import ru.mkn.krogue.model.game.Unit
import ru.mkn.krogue.model.map.Direction
import ru.mkn.krogue.model.map.Position
import ru.mkn.krogue.model.map.Tile

internal class Flee : Behavior {
    override fun doTurn(
        context: Context,
        unit: Unit,
    ): Position =
        context.run {
            val pos = unit.position
            val dirs = mutableListOf<Direction>()
            val dx = player.position.x - unit.position.x
            if (dx == 0) {
                dirs.addAll(listOf(Direction.RIGHT, Direction.LEFT))
            } else if (dx < 0) {
                dirs.add(Direction.RIGHT)
            } else { // dx > 0
                dirs.add(Direction.LEFT)
            }
            val dy = player.position.y - unit.position.y
            if (dy == 0) {
                dirs.addAll(listOf(Direction.UP, Direction.DOWN))
            } else if (dy < 0) {
                dirs.add(Direction.DOWN)
            } else { // dy > 0
                dirs.add(Direction.UP)
            }
            dirs.map { pos + it }.firstOrNull { map.tiles[it] == Tile.FLOOR && isFree(it) }
                ?: if (pos.distance(player.position) == 1) {
                    player.position
                } else {
                    pos
                }
        }
}
