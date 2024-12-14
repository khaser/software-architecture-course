package ru.mkn.krogue.graphics

import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.graphics.Symbols

val FloorTile =
    Tile.newBuilder()
        .withCharacter(Symbols.INTERPUNCT)
        .withForegroundColor(TileColor.fromString("#75715E"))
        .withBackgroundColor(TileColor.fromString("#1e2320"))
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
        .withForegroundColor(TileColor.fromString("#75715E"))
        .withBackgroundColor(TileColor.fromString("#3E3D32"))
        .buildCharacterTile()

val ArmorTile =
    Tile.newBuilder()
        .withCharacter('(')
        .withForegroundColor(TileColor.fromString("#75715E"))
        .withBackgroundColor(TileColor.fromString("#1e2320"))
        .buildCharacterTile()

val WeaponTile =
    Tile.newBuilder()
        .withCharacter(']')
        .withForegroundColor(TileColor.fromString("#75715E"))
        .withBackgroundColor(TileColor.fromString("#1e2320"))
        .buildCharacterTile()

val MobTile =
    Tile.newBuilder()
        .withCharacter('M')
        .withForegroundColor(TileColor.fromString("#75715E"))
        .withBackgroundColor(TileColor.fromString("#1e2320"))
        .buildCharacterTile()

val EmptyTile = Tile.empty()
