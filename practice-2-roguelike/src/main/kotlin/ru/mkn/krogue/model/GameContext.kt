package ru.mkn.krogue.model

import ru.mkn.krogue.model.map.Map
import ru.mkn.krogue.model.mobs.Mob
import ru.mkn.krogue.model.player.Player

data class GameContext(
    val player: Player,
    val map: Map,
    val mobs: List<Mob>,
) {
    companion object {
        fun mapFromFile(fn: String): Map = TODO()
        fun generate(
            width: Int,
            height: Int,
        ): Map = TODO()
    }
}
