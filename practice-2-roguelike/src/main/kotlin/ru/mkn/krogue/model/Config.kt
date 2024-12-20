package ru.mkn.krogue.model

import org.hexworks.zircon.api.data.Size
import ru.mkn.krogue.model.mobs.FantasyMobFactory

object Config {
    val mapSize = Size.create(80, 50)

    val mobCount = 10
    val mobIntuitionDistance = 15

    object Player {
        val hp: Int = 10
        val temper: Int = 3

        object Experience {
            fun pointsToUpgrade(level: Int) = (level + 1) * 10
        }

        val regenHpCycle = 10
        val attack = 1
        val defense = 0
    }

    object Mobs {
        val confusingProb = 0.5
        val confusingTurnCount = 2

        val factory = ::FantasyMobFactory
    }
}
