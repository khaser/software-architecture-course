package ru.mkn.krogue.model.mobs

import ru.mkn.krogue.model.game.Context
import ru.mkn.krogue.model.map.Position
import kotlin.random.Random

interface MobFactory {
    val context: Context
    val registerMob: (Mob) -> kotlin.Unit

    fun newAverageEnemy(position: Position): AverageEnemy

    fun newStaticEnemy(position: Position): StaticEnemy

    fun newPeacefulInhabitant(position: Position): PeacefulInhabitant

    fun newCowardEnemy(position: Position): CowardEnemy

    fun newReplicantEnemy(position: Position): ReplicantEnemy

    fun newRandomMob(position: Position): Mob =
        when (Random.nextInt(mobClassCount)) {
            0 -> newAverageEnemy(position)
            1 -> newStaticEnemy(position)
            2 -> newPeacefulInhabitant(position)
            3 -> newCowardEnemy(position)
            4 -> newReplicantEnemy(position)
            else -> throw IllegalArgumentException("There is no so many mob classes")
        }

    companion object {
        val mobClassCount = 5
    }
}

class FantasyMobFactory(override val context: Context, override val registerMob: (Mob) -> kotlin.Unit) : MobFactory {
    override fun newAverageEnemy(position: Position): AverageEnemy = AverageEnemy(context, position, MobAppearance.ZOMBIE)

    override fun newStaticEnemy(position: Position): StaticEnemy = StaticEnemy(context, position, MobAppearance.GIANT_SUNDEW)

    override fun newPeacefulInhabitant(position: Position): PeacefulInhabitant =
        PeacefulInhabitant(context, position, MobAppearance.GRID_BUG)

    override fun newCowardEnemy(position: Position): CowardEnemy = CowardEnemy(context, position, MobAppearance.DWARF)

    override fun newReplicantEnemy(position: Position): ReplicantEnemy = ReplicantEnemy(context, position, MobAppearance.SLIME, registerMob)
}

class ScientificMobFactory(override val context: Context, override val registerMob: (Mob) -> kotlin.Unit) : MobFactory {
    override fun newAverageEnemy(position: Position): AverageEnemy = AverageEnemy(context, position, MobAppearance.ROBOT)

    override fun newStaticEnemy(position: Position): StaticEnemy = StaticEnemy(context, position, MobAppearance.WIRE)

    override fun newPeacefulInhabitant(position: Position): PeacefulInhabitant = PeacefulInhabitant(context, position, MobAppearance.ALIEN)

    override fun newCowardEnemy(position: Position): CowardEnemy = CowardEnemy(context, position, MobAppearance.RANGER)

    override fun newReplicantEnemy(position: Position): ReplicantEnemy = ReplicantEnemy(context, position, MobAppearance.CLONE, registerMob)
}
