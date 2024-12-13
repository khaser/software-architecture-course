package ru.mkn.krogue.model

sealed interface Item

data class Armor(val ac: Int) : Item

data class Weapon(val at: Int) : Item