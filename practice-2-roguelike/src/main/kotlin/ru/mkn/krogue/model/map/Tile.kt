package ru.mkn.krogue.model.map

import kotlinx.serialization.Serializable

@Serializable
enum class Tile {
    FLOOR,
    WALL,
}
