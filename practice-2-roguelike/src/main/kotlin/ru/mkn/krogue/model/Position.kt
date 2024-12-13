package ru.mkn.krogue.model

import kotlinx.serialization.Serializable
import kotlin.math.abs

@Serializable
data class Position(val x: Int, val y: Int) {
    fun distance(oth: Position) = abs(x - oth.x) + abs(y - oth.y)
}
