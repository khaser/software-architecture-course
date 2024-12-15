package ru.mkn.krogue.model.map

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.hexworks.zircon.api.data.Size
import ru.mkn.krogue.model.Item
import java.nio.file.Path

@Serializable
data class StoredMap(val size: Size, val tiles: List<Pair<Position, Tile>>, val items: List<Pair<Position, List<Item>>>)

data class Map(val size: Size, val tiles: MutableMap<Position, Tile>, val items: MutableMap<Position, MutableList<Item>>) {
    fun saveToFile(path: Path) {
        val storedMap = StoredMap(size, tiles.toList(), items.toList())
        val json = Json.encodeToString(storedMap)
        path.toFile().printWriter().use { out ->
            out.println(json)
        }
    }

    fun getFreeAdjacentTiles(pos: Position): List<Position> = pos.adjacentBySidePositions().filter { tiles[it] == Tile.FLOOR }

    fun getRandomFreePosition(occupiedPositions: MutableSet<Position>): Position {
        val pos =
            size.fetchModelPositions().shuffled().first { pos ->
                tiles[pos] != Tile.WALL && !items.containsKey(pos) && !occupiedPositions.contains(pos)
            }
        occupiedPositions.add(pos)
        return pos
    }

    companion object {
        fun loadFromFile(path: Path): Map {
            val storedMap =
                path.toFile().reader().use { r ->
                    Json.decodeFromString<StoredMap>(r.readText())
                }
            return Map(
                storedMap.size,
                storedMap.tiles.toMap().toMutableMap(),
                storedMap.items.associate {
                    Pair(
                        it.first,
                        it.second.toMutableList(),
                    )
                }.toMutableMap(),
            )
        }

        fun generate(size: Size): Map = MapBuilder(size).makeCaves()
    }
}
