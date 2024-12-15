package ru.mkn.krogue.graphics.tile

import org.hexworks.zircon.api.GraphicalTilesetResources
import org.hexworks.zircon.api.data.Tile

val DaggerTile =
    Tile.newBuilder()
        .withName("Dagger")
        .withTileset(GraphicalTilesetResources.nethack16x16())
        .buildGraphicalTile()

val JacketTile =
    Tile.newBuilder()
        .withName("Leather jacket")
        .withTileset(GraphicalTilesetResources.nethack16x16())
        .buildGraphicalTile()
