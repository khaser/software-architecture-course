package ru.mkn.krogue.model.map

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.mkn.krogue.model.Item
import ru.mkn.krogue.model.Position
import java.nio.file.Path
import kotlin.collections.Map as HashMap

@Serializable
data class StoredMap(val tiles: List<Pair<Position, Tile>>, val items: List<Pair<Position, List<Item>>>)

data class Map(val tiles: HashMap<Position, Tile>, val items: HashMap<Position, List<Item>>) {
    fun saveToFile(path: Path) {
        val storedMap = StoredMap(tiles.toList(), items.toList())
        val json = Json.encodeToString(storedMap)
        path.toFile().printWriter().use { out ->
            out.println(json)
        }
    }

    companion object {
        fun loadFromFile(path: Path): Map {
            val storedMap =
                path.toFile().reader().use { r ->
                    Json.decodeFromString<StoredMap>(r.readText())
                }
            return Map(storedMap.tiles.toMap(), storedMap.items.toMap())
        }

        fun generate(
            width: Int,
            height: Int,
        ): Map {
            TODO("unimplemented")
        }
    }
}
