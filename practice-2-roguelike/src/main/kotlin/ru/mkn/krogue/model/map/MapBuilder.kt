package ru.mkn.krogue.model.map

import org.hexworks.zircon.api.data.Size
import ru.mkn.krogue.model.*
import kotlin.random.Random

class MapBuilder(val size: Size) {
    private var tiles: MutableMap<Position, Tile> = mutableMapOf()
    private var items = mapOf<Position, List<Item>>()
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
            freeTiles.take((percentage * freeTiles.size).toInt()).map { pos ->
                pos to listOf(Random.bernoulli(Armor(Random.nextInt(5)), Weapon(Random.nextInt(5))))
            }.toMap()
        return this
    }

    private fun MutableMap<Position, Tile>.whenPresent(
        pos: Position,
        fn: (Tile) -> Unit,
    ) {
        this[pos]?.let(fn)
    }
}
