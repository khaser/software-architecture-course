package ru.mkn.krogue.model

data class GameUnit(
    var position: Position,
    var hp: Int,
    val tempo: Int,
)