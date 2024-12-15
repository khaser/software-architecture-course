package ru.mkn.krogue.model.events

import ru.mkn.krogue.model.GameContext
import ru.mkn.krogue.model.fight
import ru.mkn.krogue.model.mobs.Mob

data class MobTurn(
    val mob: Mob,
) : GameEvent {
    override fun execute(context: GameContext): Int {
        context.run {
            val mobTurn = mob.doTurn()
            if (mobTurn == player.position) {
                // TODO: fight logic
                fight(mob, player)
            } else {
                mob.position = mobTurn
            }
            return mob.tempo
        }
    }
}
