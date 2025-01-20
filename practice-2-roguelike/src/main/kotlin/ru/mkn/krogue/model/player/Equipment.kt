package ru.mkn.krogue.model.player

import ru.mkn.krogue.model.Armor
import ru.mkn.krogue.model.Weapon

data class Equipment(
    var armor: Armor = Armor.Jacket,
    var weapon: Weapon = Weapon.Club,
)
