package ru.mkn.krogue.model.map

import org.hexworks.zircon.api.data.Size
import ru.mkn.krogue.model.*
import kotlin.random.Random

class MapBuilder(val size: Size) {
    private var tiles: MutableMap<Position, Tile> = mutableMapOf()
    private var items = mutableMapOf<Position, MutableList<Item>>()
    private val positions = size.fetchModelPositions()

    fun makeCaves(): Map {
        return randomizeTiles()
            .smooth(8)
            .genItems(0.01)
            .build()
    }

    private fun build(): Map =
        Map(
            size,
            tiles,
            items,
        )

    private fun randomizeTiles(): MapBuilder {
        positions.forEach { pos ->
            tiles[pos] = Random.bernoulli(Tile.FLOOR, Tile.WALL)
        }
        return this
    }

    private fun smooth(iterations: Int): MapBuilder {
        val newBlocks = mutableMapOf<Position, Tile>()
        repeat(iterations) {
            positions.forEach { pos ->
                var floors = 0
                var rocks = 0
                pos.adjacentPositions().plus(pos).forEach { neighbor ->
                    tiles.whenPresent(neighbor) { tile ->
                        when (tile) {
                            Tile.FLOOR -> floors++
                            Tile.WALL -> rocks++
                        }
                    }
                }
                newBlocks[pos] =
                    if (floors >= rocks) Tile.FLOOR else Tile.WALL
            }
            tiles = newBlocks
        }
        return this
    }

    private fun genItems(percentage: Double): MapBuilder {
        val freeTiles =
            positions.filter { pos ->
                tiles.getOrDefault(pos, Tile.WALL) == Tile.FLOOR
            }.shuffled()
        items =
            freeTiles.take((percentage * freeTiles.size).toInt()).associateWith {
                val p: Item = Random.bernoulli(Armor.LeatherArmor, Weapon.Dagger)
                mutableListOf(p)
            }.toMutableMap()
        return this
    }

    private fun MutableMap<Position, Tile>.whenPresent(
        pos: Position,
        fn: (Tile) -> Unit,
    ) {
        this[pos]?.let(fn)
    }
}
