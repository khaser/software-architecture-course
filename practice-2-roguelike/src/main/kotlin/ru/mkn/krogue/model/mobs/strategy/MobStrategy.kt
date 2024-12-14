package ru.mkn.krogue.model.mobs.strategy

import ru.mkn.krogue.model.GameContext
import ru.mkn.krogue.model.GameUnit
import ru.mkn.krogue.model.map.Position

abstract class MobStrategy(
    val context: GameContext,
    val unit: GameUnit,
) {
    abstract fun doTurn(): Position
}
