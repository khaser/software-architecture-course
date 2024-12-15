package ru.mkn.krogue.model.fight

interface Fighter {
    val attack: Int

    fun takeDamage(fighter: Fighter)

    fun dealDamage(fighter: Fighter) {
        fighter.takeDamage(this)
    }
}
