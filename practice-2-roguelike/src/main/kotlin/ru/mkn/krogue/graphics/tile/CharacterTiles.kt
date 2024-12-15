package ru.mkn.krogue.graphics.tile

import org.hexworks.zircon.api.color.ANSITileColor
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.graphics.Symbols
import ru.mkn.krogue.graphics.tile.Color.FLOOR_BACKGROUND

object Color {
    val FLOOR_BACKGROUND = TileColor.fromString("#1e2320")
}

val FloorTile =
    Tile.newBuilder()
        .withCharacter(Symbols.INTERPUNCT)
        .withForegroundColor(TileColor.fromString("#75715E"))
        .withBackgroundColor(FLOOR_BACKGROUND)
        .buildCharacterTile()

val WallTile =
    Tile.newBuilder()
        .withCharacter('#')
        .withForegroundColor(TileColor.fromString("#75715E"))
        .withBackgroundColor(TileColor.fromString("#3E3D32"))
        .buildCharacterTile()

val PlayerTile =
    Tile.newBuilder()
        .withCharacter('@')
        .withForegroundColor(TileColor.fromString("#FFCD22"))
        .withBackgroundColor(FLOOR_BACKGROUND)
        .buildCharacterTile()

val ArmorTile =
    Tile.newBuilder()
        .withCharacter('[')
        .withForegroundColor(ANSITileColor.WHITE)
        .withBackgroundColor(FLOOR_BACKGROUND)
        .buildCharacterTile()

val WeaponTile =
    Tile.newBuilder()
        .withCharacter('(')
        .withForegroundColor(ANSITileColor.WHITE)
        .withBackgroundColor(FLOOR_BACKGROUND)
        .buildCharacterTile()

val MobTile =
    Tile.newBuilder()
        .withCharacter('M')
        .withForegroundColor(TileColor.fromString("#85DD1B"))
        .withBackgroundColor(FLOOR_BACKGROUND)
        .buildCharacterTile()

val EmptyTile = Tile.empty()
