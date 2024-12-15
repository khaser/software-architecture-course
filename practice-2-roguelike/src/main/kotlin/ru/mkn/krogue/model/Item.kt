package ru.mkn.krogue.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface Item {
    val name: String
}

@Serializable
enum class Armor(val ac: Int) : Item {
    Jacket(2),
}

@Serializable
enum class Weapon(val at: Int) : Item {
    Dagger(2),
}
