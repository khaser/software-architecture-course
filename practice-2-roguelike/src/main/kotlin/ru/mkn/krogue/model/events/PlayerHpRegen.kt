package ru.mkn.krogue.model.events

import ru.mkn.krogue.model.Config
import ru.mkn.krogue.model.GameContext
import ru.mkn.krogue.model.player.Player
import kotlin.math.min

class PlayerHpRegen : GameEvent {
    override fun execute(context: GameContext): Int =
        context.run {
            player.hp = min(player.maxHp, player.hp + 1)
            Config.Player.hpRegenTempo
        }

    override fun equals(other: Any?): Boolean {
        return this === other
    }

    override fun hashCode(): Int {
        return System.identityHashCode(this)
    }
}
