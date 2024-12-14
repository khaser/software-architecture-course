package ru.mkn.krogue.model

import ru.mkn.krogue.model.map.Position

data class GameUnit(
    var position: Position,
    var hp: Int,
    val tempo: Int,
)
