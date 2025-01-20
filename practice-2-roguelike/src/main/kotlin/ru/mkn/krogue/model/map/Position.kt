package ru.mkn.krogue.model.map

import kotlinx.serialization.Serializable
import kotlin.math.abs

@Serializable
data class Position(val x: Int, val y: Int) {
    fun distance(oth: Position) = abs(x - oth.x) + abs(y - oth.y)

    fun adjacentBySidePositions(): List<Position> =
        listOf(
            Position(x - 1, y),
            Position(x + 1, y),
            Position(x, y - 1),
            Position(x, y + 1),
        )

    fun adjacentPositions(): List<Position> {
        return (-1..1).flatMap { x ->
            (-1..1).map { y ->
                Position(this.x + x, this.y + y)
            }
        }.minus(this).shuffled()
    }

    operator fun plus(dir: Direction): Position =
        when (dir) {
            Direction.UP -> Position(x, y - 1)
            Direction.DOWN -> Position(x, y + 1)
            Direction.LEFT -> Position(x - 1, y)
            Direction.RIGHT -> Position(x + 1, y)
        }
}
