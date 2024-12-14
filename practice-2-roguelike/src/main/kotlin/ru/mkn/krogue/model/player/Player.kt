package ru.mkn.krogue.model.player

import ru.mkn.krogue.model.GameUnit
import ru.mkn.krogue.model.map.Position

data class Player(
    val unit: GameUnit,
) {
    var position: Position
        get() = unit.position
        set(value) {
            unit.position = value
        }

    var hp: Int
        get() = unit.hp
        set(value) {
            unit.hp = value
        }
}
