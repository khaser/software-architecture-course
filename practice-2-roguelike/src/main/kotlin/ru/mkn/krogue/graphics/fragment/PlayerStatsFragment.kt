package ru.mkn.krogue.graphics.fragment

import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.component.Label
import org.hexworks.zircon.api.screen.Screen
import ru.mkn.krogue.graphics.dialog.LevelUpDialog
import ru.mkn.krogue.model.game.Config.Player.Experience.pointsToUpgrade
import ru.mkn.krogue.model.player.LevelUPStat
import ru.mkn.krogue.model.player.Player

class PlayerStatsFragment(
    private val screen: Screen,
    val player: Player,
    val width: Int,
    private val onGainLevel: (LevelUPStat) -> Unit,
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
    private var oldLevel = player.experience.level
    private val expLevel = statLabel("Level: ${player.experience.level}")
    private val expPoints = statLabel("XP: ${player.experience.points}/${pointsToUpgrade(player.experience.level)}")

    fun updateStats() {
        healthPoint.text = "HP: ${player.hp}"
        attack.text = "ATK: ${player.attack}"
        defence.text = "DEF: ${player.armor}"
        expLevel.text = "Level: ${player.experience.level}"
        expPoints.text = "XP: ${player.experience.points}/${pointsToUpgrade(player.experience.level)}"
        equipmentFragment.updateEquipment()
        if (oldLevel < player.experience.level) {
            oldLevel = player.experience.level
            screen.openModal(LevelUpDialog(screen, player, onGainLevel))
        }
    }

    override val root =
        Components.vbox()
            .withSize(width, 30)
            .withSpacing(1)
            .build().apply {
                addComponent(
                    Components.vbox()
                        .withSize(width, 5).build().apply {
                            addComponent(
                                Components.textBox(width)
                                    .addHeader("Player"),
                            )
                            addComponents(healthPoint, attack, defence)
                        },
                )
                addComponent(
                    Components.vbox()
                        .withSize(width, 5).build().apply {
                            addComponent(
                                Components.textBox(width)
                                    .addHeader("Experience"),
                            )
                            addComponents(expLevel, expPoints)
                        },
                )
                addComponent(equipmentFragment.toComponent())
            }
}
