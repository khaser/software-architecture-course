package ru.mkn.krogue.model.mobs

import ru.mkn.krogue.model.fight.Fighter
import ru.mkn.krogue.model.game.Context
import ru.mkn.krogue.model.game.Unit
import ru.mkn.krogue.model.mobs.strategy.MobStrategyKind
import kotlin.random.Random

class MobReplicator(
    context: Context,
    unit: Unit,
    appearance: MobAppearance,
    xp: Int,
    strategyKind: MobStrategyKind,
    private val spawnMob: (Boolean) -> (Mob) -> kotlin.Unit,
) : Mob(context, unit, appearance, xp, strategyKind), Fighter, Cloneable {
    override fun dealDamage(fighter: Fighter) =
        strategy.context.run {
            if (Random.nextDouble() > 0.5) {
                map.getFreeAdjacentTiles(position).firstOrNull { isFree(it) }?.let {
                    val clone = this@MobReplicator.clone()
                    clone.position = it
                    spawnMob(true)(clone)
                }
            }
            super<Mob>.dealDamage(fighter)
        }

    override fun clone(): MobReplicator {
        val unitClone = Unit(position, maxHp, tempo, regenHpCycle, baseAttack, baseDefense)
        val context = strategy.context
        return MobReplicator(context, unitClone, appearance, xp, strategyKind, spawnMob)
    }
}
