package ru.mkn.krogue.graphics

import kotlinx.collections.immutable.persistentMapOf
import org.hexworks.zircon.api.data.BlockTileType
import org.hexworks.zircon.api.data.CharacterTile
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.data.base.BaseBlock
import ru.mkn.krogue.graphics.tile.EmptyTile

class Block(val tile: CharacterTile) : BaseBlock<Tile>(EmptyTile, persistentMapOf(BlockTileType.CONTENT to tile)) {
    fun update() {
        TODO("Unimplemented")
    }

    companion object {
        fun create(tile: CharacterTile) = Block(tile)
    }
}
