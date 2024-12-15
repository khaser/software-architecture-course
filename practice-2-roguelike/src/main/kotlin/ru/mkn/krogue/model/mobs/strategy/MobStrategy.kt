package ru.mkn.krogue.model.mobs.strategy

import ru.mkn.krogue.model.GameContext
import ru.mkn.krogue.model.GameUnit
import ru.mkn.krogue.model.map.Position

enum class MobStrategyKind {
    STATIC_DAMAGE_DEALER,
    PLAYER_CHASER,
    PEACEFUL_INHABITANT,
}

sealed class MobStrategy(
    val context: GameContext,
    val unit: GameUnit,
) {
    abstract fun doTurn(): Position

    companion object {
        fun fromKind(
            kind: MobStrategyKind,
            context: GameContext,
            unit: GameUnit,
        ): MobStrategy {
            return when (kind) {
                MobStrategyKind.STATIC_DAMAGE_DEALER -> StaticDamageDealer(context, unit)
                MobStrategyKind.PEACEFUL_INHABITANT -> PeacefulInhabitant(context, unit)
                MobStrategyKind.PLAYER_CHASER -> PlayerChaser(context, unit)
            }
        }
    }
}
