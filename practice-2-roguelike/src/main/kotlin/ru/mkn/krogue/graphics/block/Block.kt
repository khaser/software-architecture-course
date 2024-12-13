package ru.mkn.krogue.graphics.block

import kotlinx.collections.immutable.persistentMapOf
import org.hexworks.zircon.api.data.BlockTileType
import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.base.BaseBlock
import ru.mkn.krogue.graphics.tile.EmptyTile
import ru.mkn.krogue.graphics.tile.OTile

class Block(val pos: Position3D, val tile: OTile) : BaseBlock<Tile>(EmptyTile.tile, persistentMapOf(BlockTileType.CONTENT to tile.tile)) {
    fun update() {
        TODO("Unimplemented")
    }
}
