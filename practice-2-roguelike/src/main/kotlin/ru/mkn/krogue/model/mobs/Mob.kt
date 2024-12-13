package ru.mkn.krogue.model.mobs

import ru.mkn.krogue.model.GameContext
import ru.mkn.krogue.model.GameUnit
import ru.mkn.krogue.model.Position
import ru.mkn.krogue.model.mobs.strategy.MobStrategy
import ru.mkn.krogue.model.mobs.strategy.StaticMobStrategy

enum class MobAppearance {
    ZOMBIE,
}
class Mob (
    val appearance: MobAppearance,
    private val strategy: MobStrategy,
) {
    var unit = strategy.unit
    fun doTurn(): Position = strategy.doTurn()
    companion object {
        fun new(mobFlavour: MobAppearance, context: GameContext, position: Position): Mob {
            return when (mobFlavour) {
                MobAppearance.ZOMBIE -> {
                    val unit = GameUnit(position, 5, 3)
                    Mob(MobAppearance.ZOMBIE, StaticMobStrategy(context, unit))
                }
            }
        }
    }
}