package ru.mkn.krogue.model

import ru.mkn.krogue.model.map.Position

open class GameUnit(
    var position: Position,
    var hp: Int,
    val tempo: Int,
) {
//    constructor(u: GameUnit): this(u.position, u.hp, u.tempo)
}
