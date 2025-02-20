package ru.mkn.krogue.model.game

import ru.mkn.krogue.model.map.Position
import kotlin.math.max
import kotlin.math.min

open class Unit(
    var position: Position,
    var maxHp: Int,
    val tempo: Int,
    val regenHpCycle: Int,
    var baseAttack: Int,
    var baseDefense: Int,
) : Cloneable {
    var hp = maxHp
    private var turnsToRegenHp: Int = regenHpCycle

    constructor(u: Unit) : this(u.position, u.maxHp, u.tempo, u.regenHpCycle, u.baseAttack, u.baseDefense)

    fun regenerateHp() {
        if (--turnsToRegenHp == 0) {
            turnsToRegenHp = regenHpCycle
            hp = min(maxHp, hp + 1)
        }
    }

    fun takeDamage(dmg: Int) {
        turnsToRegenHp = regenHpCycle
        hp -= max(dmg, 0)
    }
}
