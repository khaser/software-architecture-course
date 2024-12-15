package ru.mkn.krogue.model.player

import org.hexworks.cobalt.datatypes.Maybe
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
        get() = equipment.weapon.map { it.at }.orElse(0)

    val armor: Int
        get() = equipment.armor.map { it.ac }.orElse(0)

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
                equipment.armor.map { oldArmor ->
                    inventory.items.add(oldArmor)
                }
                equipment.armor = Maybe.of(item)
            }
            is Weapon -> {
                equipment.weapon.map { oldWeapon ->
                    inventory.items.add(oldWeapon)
                }
                equipment.weapon = Maybe.of(item)
            }
        }
    }
}
