package ru.mkn.krogue.model.events

import ru.mkn.krogue.model.GameContext
import ru.mkn.krogue.model.mobs.Mob

class MobTurn(
    private val mob: Mob,
) : GameEvent {
    override fun execute(context: GameContext): Int {
        context.run {
            val mobTurn = mob.doTurn()
            if (mobTurn == player.position) {
                // TODO: fight logic
                mob.hp--
                player.hp--
            } else {
                mob.position = mobTurn
            }
            return mob.unit.tempo
        }
    }
}
