package ru.mkn.krogue.model.mobs

import ru.mkn.krogue.model.Config
import ru.mkn.krogue.model.fight.Fighter
import ru.mkn.krogue.model.game.Context
import ru.mkn.krogue.model.game.Unit
import ru.mkn.krogue.model.map.Position
import ru.mkn.krogue.model.mobs.behavior.Roam
import ru.mkn.krogue.model.mobs.strategy.*

enum class MobAppearance {
    ZOMBIE,
    GIANT_SUNDEW,
    GRID_BUG,
    DWARF,
    SLIME,
}

open class Mob(
    context: Context,
    unit: Unit,
    val appearance: MobAppearance,
    val xp: Int,
    protected val strategyKind: MobStrategyKind,
) : Unit(unit), Fighter {
    protected val strategy = MobStrategy.fromKind(strategyKind, context, this)

    private var confusedTurnCount = 0
    override val attack: Int
        get() = baseAttack

    override fun takeDamage(fighter: Fighter) {
        takeDamage(fighter.attack - baseDefense)
    }

    fun doTurn(): Position =
        if (confusedTurnCount > 0) {
            --confusedTurnCount
            Roam().doTurn(strategy.context, this)
        } else {
            strategy.doTurn()
        }

    companion object {
        fun new(
            mobAppearance: MobAppearance,
            context: Context,
            position: Position,
            spawnMob: (Boolean) -> (Mob) -> kotlin.Unit,
        ): Mob {
            val mobConfig = Config.Mobs.mobSetup.getValue(mobAppearance)
            val mob =
                if (mobAppearance != MobAppearance.SLIME) {
                    Mob(
                        context,
                        Unit(
                            position,
                            mobConfig.hp,
                            mobConfig.tempo,
                            mobConfig.regenHpCycle,
                            mobConfig.baseAttack,
                            mobConfig.baseDefense,
                        ),
                        mobAppearance,
                        mobConfig.xp,
                        mobConfig.strategyKind,
                    )
                } else {
                    MobReplicator(
                        context,
                        Unit(
                            position,
                            mobConfig.hp,
                            mobConfig.tempo,
                            mobConfig.regenHpCycle,
                            mobConfig.baseAttack,
                            mobConfig.baseDefense,
                        ),
                        mobAppearance,
                        mobConfig.xp,
                        mobConfig.strategyKind,
                        spawnMob,
                    )
                }
            spawnMob(false)(mob)
            return mob
        }
    }
}
