package ru.mkn.krogue.model.mobs

import ru.mkn.krogue.model.Config
import ru.mkn.krogue.model.fight.Fighter
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
    regenHpCycle: Int,
    baseAttack: Int,
    baseDefense: Int,
) : Unit(position, hp, tempo, regenHpCycle, baseAttack, baseDefense), Fighter {
    private val strategy = MobStrategy.fromKind(strategyKind, context, this)
    override val attack: Int
        get() = baseAttack

    override fun takeDamage(fighter: Fighter) {
        takeDamage(fighter.attack - baseDefense)
    }

    fun doTurn(): Position = strategy.doTurn()

    companion object {
        fun new(
            mobAppearance: MobAppearance,
            context: Context,
            position: Position,
        ): Mob {
            val mobConfig = Config.Mobs.mobSetup.getValue(mobAppearance)
            return Mob(
                context,
                mobAppearance,
                mobConfig.strategyKind,
                position,
                mobConfig.hp,
                mobConfig.tempo,
                mobConfig.regenHpCycle,
                mobConfig.baseAttack,
                mobConfig.baseDefense,
            )
        }
    }
}
