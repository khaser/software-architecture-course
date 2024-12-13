package ru.mkn.krogue.model

import ru.mkn.krogue.model.map.Map
import ru.mkn.krogue.model.mobs.Mob
data class GameContext(
    val player: Player,
    val map: Map,
    val mobs: List<Mob>,
)
