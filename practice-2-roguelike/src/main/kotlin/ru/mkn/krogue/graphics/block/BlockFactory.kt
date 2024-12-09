package ru.mkn.krogue.graphics.block

import org.hexworks.zircon.api.data.Position3D
import ru.mkn.krogue.graphics.tile.OTile

object BlockFactory {
    fun createBlock(position: Position3D, tile: OTile) = Block(position, tile)
}