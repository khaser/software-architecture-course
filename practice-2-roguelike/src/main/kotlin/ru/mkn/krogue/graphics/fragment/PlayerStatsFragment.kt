package ru.mkn.krogue.graphics.fragment

import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.component.Label
import ru.mkn.krogue.model.player.Player

class PlayerStatsFragment(
    val player: Player,
    val width: Int,
) : Fragment {
    private val equipmentFragment = EquipmentFragment(player.equipment, width)

    private fun statLabel(text: String): Label =
        Components.label()
            .withSize(width, 1)
            .withText(text)
            .build()

    private val healthPoint = statLabel("HP: ${player.hp}")
    private val attack = statLabel("ATK: ${player.attack}")
    private val defence = statLabel("DEF: ${player.armor}")

    fun updateStats() {
        healthPoint.text = "HP: ${player.hp}"
        attack.text = "ATK: ${player.attack}"
        defence.text = "DEF: ${player.armor}"
        equipmentFragment.updateEquipment()
    }

    override val root =
        Components.vbox()
            .withSize(width, 30)
            .build().apply {
                addComponent(
                    Components.textBox(width)
                        .addHeader("Player"),
                )
                addComponents(healthPoint, attack, defence)
                addComponent(equipmentFragment.toComponent())
            }
}
