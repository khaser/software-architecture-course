package ru.mkn.krogue.model

import ru.mkn.krogue.model.mobs.Mob
import ru.mkn.krogue.model.map.Map

enum class GameState {
    IN_PROGRESS,
    OVER
}
class GameController(
    val context: GameContext
) {
    fun resumeToPlayerTurn(): GameState {
        val playerPos = context.player.unit.position
        context.mobs.forEach { mob ->
            val mobTurn = mob.doTurn()
            if (mobTurn == playerPos) {
                // TODO: fight logic

            } else {
                mob.unit.position = mobTurn
            }
        }
        return GameState.IN_PROGRESS
    }
}