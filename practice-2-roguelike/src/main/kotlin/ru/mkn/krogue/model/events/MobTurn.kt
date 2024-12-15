package ru.mkn.krogue.model.events

import ru.mkn.krogue.model.game.Context
import ru.mkn.krogue.model.game.Logger
import ru.mkn.krogue.model.mobs.Mob

data class MobTurn(
    val mob: Mob,
) : GameEvent {
    override fun execute(
        context: Context,
        logger: Logger,
    ): Int {
        context.run {
            val mobTurn = mob.doTurn()
            if (mobTurn == player.position) {
                mob.dealDamage(player)
                logger.log("${mob.appearance} attacks player.")
            } else {
                mob.position = mobTurn
            }
            mob.regenerateHp()
            return mob.tempo
        }
    }
}
