package ru.mkn.krogue.model.mobs

import ru.mkn.krogue.model.fight.Fighter
import ru.mkn.krogue.model.game.Context
import ru.mkn.krogue.model.game.Unit
import ru.mkn.krogue.model.map.Position
import ru.mkn.krogue.model.mobs.behavior.Roam
import ru.mkn.krogue.model.mobs.strategy.*
import kotlin.random.Random

class AverageEnemy(context: Context, position: Position, override val appearance: MobAppearance) : Mob(
    context,
    Unit(position, 5, 3, 7, 1, 0),
    5,
    MobStrategyKind.PLAYER_CHASER,
)

class StaticEnemy(context: Context, position: Position, override val appearance: MobAppearance) : Mob(
    context,
    Unit(position, 1, 3, 1, 2, 0),
    1,
    MobStrategyKind.STATIC_DAMAGE_DEALER,
)

class PeacefulInhabitant(context: Context, position: Position, override val appearance: MobAppearance) : Mob(
    context,
    Unit(position, 2, 1, 5, 0, 0),
    1,
    MobStrategyKind.PEACEFUL_INHABITANT,
)

class CowardEnemy(context: Context, position: Position, override val appearance: MobAppearance) : Mob(
    context,
    Unit(position, 8, 4, 5, 1, 1),
    10,
    MobStrategyKind.WITHDRAWER,
)

class ReplicantEnemy(
    context: Context,
    position: Position,
    override val appearance: MobAppearance,
    private val registerMob: (Mob) -> kotlin.Unit,
) :
    Mob(
            context,
            Unit(position, 2, 4, 7, 1, 0),
            5,
            MobStrategyKind.PLAYER_CHASER,
        ),
        Cloneable {
    override fun dealDamage(fighter: Fighter) =
        strategy.context.run {
            if (Random.nextDouble() > 0.5) {
                map.getFreeAdjacentTiles(position).firstOrNull { isFree(it) }?.let {
                    val clone = clone()
                    clone.position = it
                    registerMob(clone)
                }
            }
            super.dealDamage(fighter)
        }

    override fun clone(): ReplicantEnemy = ReplicantEnemy(strategy.context, position, appearance, registerMob)
}

sealed class Mob(
    context: Context,
    unit: Unit,
    val xp: Int,
    strategyKind: MobStrategyKind,
) : Unit(unit), Fighter {
    abstract val appearance: MobAppearance
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
}
