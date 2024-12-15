package ru.mkn.krogue.model.player

import org.hexworks.cobalt.datatypes.Maybe
import ru.mkn.krogue.model.Armor
import ru.mkn.krogue.model.Weapon

data class Equipment(
    var armor: Maybe<Armor> = Maybe.empty(),
    var weapon: Maybe<Weapon> = Maybe.empty(),
)
