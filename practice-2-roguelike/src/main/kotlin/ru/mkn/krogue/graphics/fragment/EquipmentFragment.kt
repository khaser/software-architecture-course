package ru.mkn.krogue.graphics.fragment

import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Component
import ru.mkn.krogue.graphics.tile.itemToTile
import ru.mkn.krogue.model.player.Equipment

class EquipmentFragment(val equipment: Equipment, val width: Int) {
    private val weaponStats: String
        get() = " A: ${equipment.weapon.at}"

    private val armorStats: String
        get() = " D: ${equipment.armor.ac}"

    private val weaponIcon =
        Components.icon().withIcon(itemToTile.getValue(equipment.weapon)).build()
    private val weaponNameLabel =
        Components.label()
            .withText(equipment.weapon.name)
            .withSize(width - 2, 1)
            .build()
    private val weaponStatsLabel =
        Components.label()
            .withText(weaponStats)
            .withSize(width - 1, 1)
            .build()

    private val armorIcon = Components.icon().withIcon(itemToTile.getValue(equipment.armor)).build()
    private val armorNameLabel =
        Components.label()
            .withText(equipment.armor.name)
            .withSize(width - 2, 1)
            .build()
    private val armorStatsLabel =
        Components.label()
            .withText(armorStats)
            .withSize(width - 1, 1)
            .build()

    fun updateEquipment() {
        equipment.run {
            armorNameLabel.text = armor.name
            armorIcon.icon = itemToTile.getValue(equipment.armor)
            armorStatsLabel.text = armorStats

            weaponNameLabel.text = weapon.name
            weaponIcon.icon = itemToTile.getValue(equipment.weapon)
            weaponStatsLabel.text = weaponStats
        }
    }

    fun toComponent(): Component {
        return Components.textBox(width)
            .addHeader("Weapon", withNewLine = false)
            .addInlineComponent(weaponIcon)
            .addInlineComponent(weaponNameLabel)
            .commitInlineElements()
            .addInlineComponent(weaponStatsLabel)
            .commitInlineElements()
            .addNewLine()
            .addHeader("Armor", withNewLine = false)
            .addInlineComponent(armorIcon)
            .addInlineComponent(armorNameLabel)
            .commitInlineElements()
            .addInlineComponent(armorStatsLabel)
            .commitInlineElements()
            .build()
    }
}
