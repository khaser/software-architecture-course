package ru.mkn.krogue.model

import org.hexworks.zircon.api.data.Size
import ru.mkn.krogue.model.mobs.MobAppearance
import ru.mkn.krogue.model.mobs.strategy.MobStrategyKind

object Config {
    val mapSize = Size.create(80, 50)

    val mobCount = 10
    val mobIntuitionDistance = 15

    object Player {
        val hp: Int = 10
        val temper: Int = 3

        object Experience

        val regenHpCycle = 10
        val attack = 1
        val defense = 0
    }

    object Mobs {
        data class MobConfig(
            val strategyKind: MobStrategyKind,
            val hp: Int,
            val tempo: Int,
            val regenHpCycle: Int,
            val baseAttack: Int,
            val baseDefense: Int,
        )

        val mobSetup =
            mapOf(
                MobAppearance.ZOMBIE to MobConfig(MobStrategyKind.PLAYER_CHASER, 5, 3, 7, 1, 0),
                MobAppearance.GIANT_SUNDEW to MobConfig(MobStrategyKind.STATIC_DAMAGE_DEALER, 1, 3, 1, 2, 0),
                MobAppearance.GRID_BUG to MobConfig(MobStrategyKind.PEACEFUL_INHABITANT, 2, 1, 5, 0, 0),
            )
    }
}
