package ru.mkn.krogue.graphics

import org.hexworks.zircon.api.builder.game.GameAreaBuilder
import org.hexworks.zircon.api.data.Position3D
import org.hexworks.zircon.api.data.Size3D
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.game.GameArea
import ru.mkn.krogue.model.Armor
import ru.mkn.krogue.model.GameContext
import ru.mkn.krogue.model.Weapon
import ru.mkn.krogue.model.map.Position
import kotlin.collections.Map as HashMap

class World(
    private val gameContext: GameContext,
) : GameArea<Tile, Block> by GameAreaBuilder.newBuilder<Tile, Block>()
        .withVisibleSize(Size3D.from2DSize(ViewConfig.GameArea.size, 1))
        .withActualSize(Size3D.from2DSize(ViewConfig.World.size, 1))
        .build() {
    private val map: HashMap<Position, Position3D> =
        actualSize.fetchPositions().map { pos -> Position(pos.x, pos.y) to pos }.toMap()

    init {
        map.values.forEach { pos ->
            setBlockAt(pos, Block.create(EmptyTile))
        }
    }

    fun update() {
        gameContext.map.tiles.forEach { (pos, tile) ->
            val viewPos = map[pos] ?: throw IllegalArgumentException("Unexpected $pos position")
            val baseBlockTile =
                listOf(
                    when (tile) {
                        ru.mkn.krogue.model.map.Tile.FLOOR -> FloorTile
                        ru.mkn.krogue.model.map.Tile.WALL -> WallTile
                    },
                )
            val items =
                gameContext.map.items.getOrDefault(pos, listOf()).map {
                    when (it) {
                        is Armor -> ArmorTile
                        is Weapon -> WeaponTile
                    }
                }
            val mob = gameContext.mobs.find { it.position == pos }?.let { listOf(MobTile) } ?: listOf()
            val player =
                if (gameContext.player.position == pos) {
                    listOf(PlayerTile)
                } else {
                    listOf()
                }
            val block = (player + mob + items + baseBlockTile).first()
            setBlockAt(viewPos, Block.create(block))
        }
    }
}
