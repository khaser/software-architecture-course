package ru.mkn.krogue.model.mobs

import ru.mkn.krogue.model.GameContext
import ru.mkn.krogue.model.GameUnit
import ru.mkn.krogue.model.map.Position
import ru.mkn.krogue.model.mobs.strategy.MobStrategy
import ru.mkn.krogue.model.mobs.strategy.StaticMobStrategy

enum class MobAppearance {
    ZOMBIE,
}

class Mob(
    val appearance: MobAppearance,
    private val strategy: MobStrategy,
) {
    val unit: GameUnit = strategy.unit

    var position: Position
        get() = unit.position
        set(value) {
            unit.position = value
        }
    var hp: Int
        get() = unit.hp
        set(value) {
            unit.hp = value
        }

    fun doTurn(): Position = strategy.doTurn()

    companion object {
        fun new(
            mobFlavour: MobAppearance,
            context: GameContext,
            position: Position,
        ): Mob {
            return when (mobFlavour) {
                MobAppearance.ZOMBIE -> {
                    val unit = GameUnit(position, 5, 3)
                    Mob(MobAppearance.ZOMBIE, StaticMobStrategy(context, unit))
                }
            }
        }
    }
}
