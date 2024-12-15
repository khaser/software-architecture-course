package ru.mkn.krogue.model

import org.hexworks.zircon.api.data.Size

object Config {
    val mapSize = Size.create(80, 50)

    val mobCount = 10
    val mobIntuitionDistance = 15

    object Player {
        val hp: Int = 10
        val temper: Int = 3
        val hpRegenTempo = 30

        object Experience
    }
}
