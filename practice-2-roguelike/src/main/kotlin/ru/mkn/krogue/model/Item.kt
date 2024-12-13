package ru.mkn.krogue.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface Item

@Serializable
data class Armor(val ac: Int) : Item

@Serializable
data class Weapon(val at: Int) : Item
