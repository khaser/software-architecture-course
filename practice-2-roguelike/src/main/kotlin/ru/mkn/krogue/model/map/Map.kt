package ru.mkn.krogue.model.map

import kotlinx.serialization.Serializable
import ru.mkn.krogue.model.Item
import ru.mkn.krogue.model.Position
import kotlin.collections.Map

@Serializable
data class Map(val tiles: Map<Position, Tile>, val items: Map<Position, List<Item>>) {
}