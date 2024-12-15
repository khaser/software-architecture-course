package ru.mkn.krogue.model

fun fight(
    attacker: GameUnit,
    target: GameUnit,
) {
    target.hp--
}
