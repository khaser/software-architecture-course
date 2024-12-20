package ru.mkn.krogue.model

import org.hexworks.zircon.api.data.Size
import ru.mkn.krogue.model.mobs.FantasyMobFactory

object Config {
    val mapSize = Size.create(80, 50)

    const val MOB_COUNT = 10
    const val MOB_INTUITION_DISTANCE = 15

    object Player {
        const val HP: Int = 10
        const val TEMPO: Int = 3

        object Experience {
            fun pointsToUpgrade(level: Int) = (level + 1) * 10
        }

        const val REGEN_HP_CYCLE = 10
        const val ATTACK = 1
        const val DEFENCE = 0
    }

    object Mobs {
        const val CONFUSING_PROB = 0.1
        const val CONFUSING_TURN_COUNT = 2

        val factory = ::FantasyMobFactory
    }
}
