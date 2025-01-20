package ru.mkn.krogue.model.mobs

import ru.mkn.krogue.model.fight.Fighter
import ru.mkn.krogue.model.game.Config
import ru.mkn.krogue.model.game.Context
import ru.mkn.krogue.model.game.Logger
import ru.mkn.krogue.model.game.Unit
import ru.mkn.krogue.model.map.Position
import ru.mkn.krogue.model.mobs.behavior.Roam
import ru.mkn.krogue.model.mobs.strategy.*
import kotlin.random.Random

class AverageEnemy(context: Context, logger: Logger, position: Position, override val appearance: MobAppearance) : Mob(
    context,
    logger,
    Unit(position, 5, 3, 7, 1, 0),
    5,
    MobStrategyKind.PLAYER_CHASER,
)

class StaticEnemy(context: Context, logger: Logger, position: Position, override val appearance: MobAppearance) : Mob(
    context,
    logger,
    Unit(position, 1, 3, 1, 2, 0),
    1,
    MobStrategyKind.STATIC_DAMAGE_DEALER,
)

class PeacefulInhabitant(context: Context, logger: Logger, position: Position, override val appearance: MobAppearance) : Mob(
    context,
    logger,
    Unit(position, 2, 1, 5, 0, 0),
    1,
    MobStrategyKind.PEACEFUL_INHABITANT,
)

class CowardEnemy(context: Context, logger: Logger, position: Position, override val appearance: MobAppearance) : Mob(
    context,
    logger,
    Unit(position, 8, 4, 5, 1, 1),
    10,
    MobStrategyKind.WITHDRAWER,
)

class ReplicantEnemy(
    context: Context,
    logger: Logger,
    position: Position,
    override val appearance: MobAppearance,
    private val registerMob: (Mob) -> kotlin.Unit,
) :
    Mob(
            context,
            logger,
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
                    logger.log("$appearance was spawned!")
                    registerMob(clone)
                }
            }
            super.dealDamage(fighter)
        }

    override fun clone(): ReplicantEnemy = ReplicantEnemy(strategy.context, logger, position, appearance, registerMob)
}

sealed class Mob(
    context: Context,
    val logger: Logger,
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
        if (Random.nextDouble() < Config.Mobs.CONFUSING_PROB) {
            confusedTurnCount = Config.Mobs.CONFUSING_TURN_COUNT
            logger.log("$appearance now is confused.")
        }
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
