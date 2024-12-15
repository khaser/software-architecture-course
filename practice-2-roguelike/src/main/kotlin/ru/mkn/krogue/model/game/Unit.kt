package ru.mkn.krogue.model.game

import ru.mkn.krogue.model.map.Position

open class Unit(
    var position: Position,
    var hp: Int,
    val tempo: Int,
) {
//    constructor(u: GameUnit): this(u.position, u.hp, u.tempo)
}
