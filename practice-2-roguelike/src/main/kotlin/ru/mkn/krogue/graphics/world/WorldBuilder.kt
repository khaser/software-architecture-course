package ru.mkn.krogue.graphics.world

import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.data.Size3D
import ru.mkn.krogue.graphics.block.Block
import ru.mkn.krogue.graphics.block.BlockFactory
import ru.mkn.krogue.graphics.tile.FloorTile
import ru.mkn.krogue.graphics.tile.WallTile

fun Position3D.sameLevelNeighborsShuffled(): List<Position3D> {
    return (-1..1).flatMap { x ->
        (-1..1).map { y ->
            this.withRelativeX(x).withRelativeY(y)
        }
    }.minus(this).shuffled()
}

class WorldBuilder(private val worldSize: Size3D) {
    private var blocks: MutableMap<Position3D, Block> = mutableMapOf()

    fun makeCaves(): WorldBuilder {
        return randomizeTiles()
            .smooth(8)
    }

    fun build(visibleSize: Size3D): World =
        World(
            blocks.mapKeys { (key, value) ->
                ru.mkn.krogue.model.Position(
                    key.x,
                    key.y,
                )
            },
            visibleSize,
            worldSize,
        )

    private fun randomizeTiles(): WorldBuilder {
        forAllPositions { pos ->
            blocks[pos] =
                if (Math.random() < 0.5) {
                    BlockFactory.createBlock(pos, FloorTile)
                } else {
                    BlockFactory.createBlock(pos, WallTile)
                }
        }
        return this
    }

    private fun smooth(iterations: Int): WorldBuilder {
        val newBlocks = mutableMapOf<Position3D, Block>()
        repeat(iterations) {
            forAllPositions { pos ->
                var floors = 0
                var rocks = 0
                pos.sameLevelNeighborsShuffled().plus(pos).forEach { neighbor ->
                    blocks.whenPresent(neighbor) { block -> // 9
                        when (block.tile) {
                            FloorTile -> floors++
                            else -> rocks++
                        }
                    }
                }
                newBlocks[pos] =
                    if (floors >= rocks) BlockFactory.createBlock(pos, FloorTile) else BlockFactory.createBlock(pos, WallTile)
            }
            blocks = newBlocks
        }
        return this
    }

    private fun forAllPositions(fn: (Position3D) -> Unit) {
        worldSize.fetchPositions().forEach(fn)
    }

    private fun MutableMap<Position3D, Block>.whenPresent(
        pos: Position3D,
        fn: (Block) -> Unit,
    ) {
        this[pos]?.let(fn)
    }
}
