package ru.mkn.krogue.model

import ru.mkn.krogue.model.game.Unit

fun fight(
    attacker: Unit,
    target: Unit,
) {
    target.hp--
}
