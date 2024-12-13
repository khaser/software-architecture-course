package ru.mkn.krogue.model.map

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.mkn.krogue.model.Item
import ru.mkn.krogue.model.Position
import java.io.File
import kotlin.collections.Map as HashMap

@Serializable
data class Map(val tiles: HashMap<Position, Tile>, val items: HashMap<Position, List<Item>>) {
    fun saveToFile(filename: String) {
        val json = Json.encodeToString(this)
        File(filename).printWriter().use { out ->
            out.println(json)
        }
    }

    companion object {
        fun loadFromFile(filename: String): Map =
            File(filename).reader().use { r ->
                Json.decodeFromString<Map>(r.readText())
            }

        fun generate(
            width: Int,
            height: Int,
        ): Map {
            TODO("unimplemented")
        }
    }
}
