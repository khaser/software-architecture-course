package ru.mkn.krogue.model.mobs.strategy

import ru.mkn.krogue.model.GameContext
import ru.mkn.krogue.model.Position
import ru.mkn.krogue.model.GameUnit
import ru.mkn.krogue.model.mobs.Mob

abstract class MobStrategy(
    val context: GameContext,
    val unit: GameUnit,
) {
    abstract fun doTurn(): Position
}