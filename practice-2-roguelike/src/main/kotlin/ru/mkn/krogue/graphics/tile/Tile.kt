package ru.mkn.krogue.graphics.tile

import org.hexworks.zircon.api.GraphicalTilesetResources
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.data.CharacterTile
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.graphics.Symbols
import ru.mkn.krogue.graphics.ViewConfig

sealed interface OTile {
    val tile: Tile
}

data object FloorTile : OTile {
    private val foregroundColor = TileColor.fromString("#75715E")
    private val backgroundColor = TileColor.fromString("#1e2320")
    private const val CHARACTER = Symbols.INTERPUNCT
    override val tile: Tile =
        Tile.newBuilder()
            .withCharacter(CHARACTER)
            .withForegroundColor(foregroundColor)
            .withBackgroundColor(backgroundColor)
            .buildCharacterTile()
}

data object WallTile : OTile {
    private val foregroundColor = TileColor.fromString("#75715E")
    private val backgroundColor = TileColor.fromString("#3E3D32")
    private const val CHARACTER = '#'
    override val tile: Tile =
        Tile.newBuilder()
            .withCharacter(CHARACTER)
            .withForegroundColor(foregroundColor)
            .withBackgroundColor(backgroundColor)
            .buildCharacterTile()

}

data object PlayerTile : OTile {
    private val foregroundColor = TileColor.fromString("#75715E")
    private val backgroundColor = TileColor.fromString("#3E3D32")
    private const val CHARACTER = '@'
    override val tile: Tile =
        Tile.newBuilder()
            .withCharacter(CHARACTER)
            .withForegroundColor(foregroundColor)
            .withBackgroundColor(backgroundColor)
            .buildCharacterTile()

}

data object EmptyTile : OTile {
    override val tile: Tile = Tile.empty()
}

