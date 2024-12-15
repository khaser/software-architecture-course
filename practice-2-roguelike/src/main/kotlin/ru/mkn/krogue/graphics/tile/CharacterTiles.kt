package ru.mkn.krogue.graphics.tile

import org.hexworks.zircon.api.color.ANSITileColor
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.graphics.Symbols
import ru.mkn.krogue.graphics.tile.Color.FLOOR_BACKGROUND
import ru.mkn.krogue.model.mobs.MobAppearance

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

val ZombieTile =
    Tile.newBuilder()
        .withCharacter('Z')
        .withForegroundColor(TileColor.fromString("#85DD1B"))
        .withBackgroundColor(FLOOR_BACKGROUND)
        .buildCharacterTile()

val GridBugTile =
    Tile.newBuilder()
        .withCharacter('B')
        .withForegroundColor(TileColor.fromString("#4287f5"))
        .withBackgroundColor(FLOOR_BACKGROUND)
        .buildCharacterTile()

val GiantSundewTile =
    Tile.newBuilder()
        .withCharacter('S')
        .withForegroundColor(TileColor.fromString("#f54242"))
        .withBackgroundColor(FLOOR_BACKGROUND)
        .buildCharacterTile()

val mobToTile =
    mapOf(
        MobAppearance.ZOMBIE to ZombieTile,
        MobAppearance.GRID_BUG to GridBugTile,
        MobAppearance.GIANT_SUNDEW to GiantSundewTile,
    )

val EmptyTile = Tile.empty()
