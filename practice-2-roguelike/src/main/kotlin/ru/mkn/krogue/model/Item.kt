package ru.mkn.krogue.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface Item {
    val name: String
}

@Serializable
enum class Armor(val ac: Int) : Item {
    Jacket(0),
    LeatherArmor(1),
}

@Serializable
enum class Weapon(val at: Int) : Item {
    Club(0),
    Dagger(1),
}
