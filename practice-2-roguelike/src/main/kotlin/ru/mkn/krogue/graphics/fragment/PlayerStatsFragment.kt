package ru.mkn.krogue.graphics.fragment

import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.builder.component.ParagraphBuilder
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.component.Paragraph
import ru.mkn.krogue.model.player.Player

class PlayerStatsFragment(
    player: Player,
    val width: Int,
) : Fragment {
    private fun statParagraph(text: String): Paragraph =
        ParagraphBuilder.newBuilder()
            .withSize(width, 1)
            .withText(text)
            .build()

    private val healthPointPar = statParagraph("HP: ${player.hp}")
    private val attackPar = statParagraph("ATK: ${player.attack}")
    private val defencePar = statParagraph("DEF: ${player.armor}")

    private val textBox =
        Components.textBox(width)
            .addParagraph(healthPointPar, true)
            .addParagraph(attackPar, true)
            .addParagraph(defencePar, true)

    fun updateStats(player: Player) {
        healthPointPar.text = "HP: ${player.hp}"
        attackPar.text = "ATK: ${player.attack}"
        defencePar.text = "DEF: ${player.armor}"
    }

    override val root =
        Components.vbox()
            .withSize(width, 30)
            .withSpacing(1)
            .build().apply {
                addComponent(Components.header().withText("Player"))
                addComponent(textBox)
            }
}
