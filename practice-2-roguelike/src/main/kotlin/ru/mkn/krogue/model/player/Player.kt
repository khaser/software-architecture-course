package ru.mkn.krogue.model.player

import ru.mkn.krogue.model.Armor
import ru.mkn.krogue.model.Config
import ru.mkn.krogue.model.Item
import ru.mkn.krogue.model.Weapon
import ru.mkn.krogue.model.fight.Fighter
import ru.mkn.krogue.model.game.Unit
import ru.mkn.krogue.model.map.Position

class Player(
    position: Position,
    hp: Int,
    tempo: Int,
    val inventory: Inventory,
    val equipment: Equipment,
    val experience: Experience,
) : Unit(position, hp, tempo, Config.Player.regenHpCycle, Config.Player.attack, Config.Player.defense), Fighter {
    override val attack: Int
        get() = baseAttack + equipment.weapon.at

    val armor: Int
        get() = baseDefense + equipment.armor.ac

    override fun takeDamage(fighter: Fighter) {
        takeDamage(fighter.attack - armor)
    }

    fun dropItem(item: Item) {
        inventory.items.remove(item)
    }

    fun equipItem(item: Item): Item {
        inventory.items.remove(item)
        return when (item) {
            is Armor -> {
                val oldArmor = equipment.armor
                inventory.items.add(oldArmor)
                equipment.armor = item
                oldArmor
            }
            is Weapon -> {
                val oldWeapon = equipment.weapon
                inventory.items.add(oldWeapon)
                equipment.weapon = item
                oldWeapon
            }
        }
    }
}
