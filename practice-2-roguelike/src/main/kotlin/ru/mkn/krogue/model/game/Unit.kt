package ru.mkn.krogue.model.game

import ru.mkn.krogue.model.map.Position
import kotlin.math.min

open class Unit(
    var position: Position,
    val maxHp: Int,
    val tempo: Int,
    val regenHpCycle: Int,
    val baseAttack: Int,
    val baseDefense: Int,
) {
    var hp = maxHp
    private var turnsToRegenHp: Int = regenHpCycle

    fun regenerateHp() {
        if (--turnsToRegenHp == 0) {
            turnsToRegenHp = regenHpCycle
            hp = min(maxHp, hp + 1)
        }
    }

    fun takeDamage(dmg: Int) {
        turnsToRegenHp = regenHpCycle
        hp -= dmg
    }
}
