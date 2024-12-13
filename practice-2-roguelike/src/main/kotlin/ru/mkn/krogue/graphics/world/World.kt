package ru.mkn.krogue.graphics.world

import org.hexworks.zircon.api.builder.game.GameAreaBuilder
import org.hexworks.zircon.api.data.Size3D
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.game.GameArea
import ru.mkn.krogue.graphics.block.Block
import ru.mkn.krogue.model.Position

class World(
    private val posToBlock: Map<Position, Block>,
    visibleSize: Size3D,
    actualSize: Size3D,
) : GameArea<Tile, Block> by GameAreaBuilder.newBuilder<Tile, Block>()
        .withVisibleSize(visibleSize)
        .withActualSize(actualSize)
        .build() {
    init {
        posToBlock.values.forEach { block ->
            setBlockAt(block.pos, block)
        }
    }

    private fun blockOf(position: Position): Block = posToBlock[position]!!

    fun update() {
        blockOf(Position(0, 0)).update()
    }
}
