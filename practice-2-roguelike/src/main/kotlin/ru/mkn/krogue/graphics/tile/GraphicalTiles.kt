package ru.mkn.krogue.graphics.tile

import org.hexworks.zircon.api.GraphicalTilesetResources
import org.hexworks.zircon.api.data.Tile
import ru.mkn.krogue.model.Armor
import ru.mkn.krogue.model.Item
import ru.mkn.krogue.model.Weapon

val DaggerTile =
    Tile.newBuilder()
        .withName("Dagger")
        .withTileset(GraphicalTilesetResources.nethack16x16())
        .buildGraphicalTile()

val ClubTile =
    Tile.newBuilder()
        .withName("Club")
        .withTileset(GraphicalTilesetResources.nethack16x16())
        .buildGraphicalTile()

val JacketTile =
    Tile.newBuilder()
        .withName("Leather jacket")
        .withTileset(GraphicalTilesetResources.nethack16x16())
        .buildGraphicalTile()

val LeatherArmorTile =
    Tile.newBuilder()
        .withName("Leather armor")
        .withTileset(GraphicalTilesetResources.nethack16x16())
        .buildGraphicalTile()

val itemToTile: Map<Item, Tile> =
    mapOf(
        Weapon.Club to ClubTile,
        Weapon.Dagger to DaggerTile,
        Armor.LeatherArmor to LeatherArmorTile,
        Armor.Jacket to JacketTile,
    )
