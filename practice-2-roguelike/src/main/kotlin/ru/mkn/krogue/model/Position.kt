package ru.mkn.krogue.model

import kotlinx.serialization.Serializable
import kotlin.math.abs

@Serializable
data class Position(val x: Int, val y: Int) {
    fun distance(oth: Position) = abs(x - oth.x) + abs(y - oth.y)
}

fun Position.adjacentPositions(): List<Position> {
    return (-1..1).flatMap { x ->
        (-1..1).map { y ->
            Position(this.x + x, this.y + y)
        }
    }.minus(this).shuffled()
}
