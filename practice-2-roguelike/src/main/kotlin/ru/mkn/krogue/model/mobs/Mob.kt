package ru.mkn.krogue.model.mobs

import ru.mkn.krogue.model.game.Context
import ru.mkn.krogue.model.game.Unit
import ru.mkn.krogue.model.map.Position
import ru.mkn.krogue.model.mobs.strategy.*

enum class MobAppearance {
    ZOMBIE,
    GIANT_SUNDEW,
    GRID_BUG,
//    LEPRECON with stealing mechanic
}

class Mob(
    context: Context,
    val appearance: MobAppearance,
    strategyKind: MobStrategyKind,
    position: Position,
    hp: Int,
    tempo: Int,
) : Unit(position, hp, tempo) {
    private val strategy = MobStrategy.fromKind(strategyKind, context, this)

    fun doTurn(): Position = strategy.doTurn()

    companion object {
        fun new(
            mobFlavour: MobAppearance,
            context: Context,
            position: Position,
        ): Mob {
            return when (mobFlavour) {
                MobAppearance.ZOMBIE -> {
                    Mob(context, MobAppearance.ZOMBIE, MobStrategyKind.PLAYER_CHASER, position, 5, 3)
                }
                MobAppearance.GIANT_SUNDEW -> {
                    Mob(context, MobAppearance.GIANT_SUNDEW, MobStrategyKind.STATIC_DAMAGE_DEALER, position, 1, 3)
                }
                MobAppearance.GRID_BUG -> {
                    Mob(context, MobAppearance.GRID_BUG, MobStrategyKind.PEACEFUL_INHABITANT, position, 1, 1)
                }
            }
        }
    }
}
