package ru.mkn.krogue.model.player

import ru.mkn.krogue.model.Armor
import ru.mkn.krogue.model.GameUnit
import ru.mkn.krogue.model.Item
import ru.mkn.krogue.model.Weapon
import ru.mkn.krogue.model.map.Position

data class Player(
    val unit: GameUnit,
    val inventory: Inventory,
    val equipment: Equipment,
) {
    var position: Position
        get() = unit.position
        set(value) {
            unit.position = value
        }

    val attack: Int
        get() = equipment.weapon.at

    val armor: Int
        get() = equipment.armor.ac

    var hp: Int
        get() = unit.hp
        set(value) {
            unit.hp = value
        }

    fun dropItem(item: Item) {
        inventory.items.remove(item)
    }

    fun equipItem(item: Item) {
        inventory.items.remove(item)
        when (item) {
            is Armor -> {
                inventory.items.add(equipment.armor)
                equipment.armor = item
            }
            is Weapon -> {
                inventory.items.add(equipment.weapon)
                equipment.weapon = item
            }
        }
    }
}
