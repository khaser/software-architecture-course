package ru.mkn.krogue.model.player

import org.hexworks.cobalt.datatypes.Maybe
import ru.mkn.krogue.model.GameUnit
import ru.mkn.krogue.model.Item
import ru.mkn.krogue.model.map.Position

data class Player(
    val unit: GameUnit,
    val inventory: Inventory,
) {
    var position: Position
        get() = unit.position
        set(value) {
            unit.position = value
        }

    val attack: Int
        get() = 0

    val armor: Int
        get() = 0

    var hp: Int
        get() = unit.hp
        set(value) {
            unit.hp = value
        }

    fun dropItem(item: Item) {
        inventory.items.remove(item)
    }

    fun equipItem(item: Item): Maybe<Item> {
        TODO("Not implemented")
    }
}
