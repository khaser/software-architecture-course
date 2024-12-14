package ru.mkn.krogue.graphics.fragment

import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import ru.mkn.krogue.model.player.Player

class PlayerStatsFragment(
    width: Int,
    player: Player,
) : Fragment {
    override val root =
        Components.vbox()
            .withSize(width, 30)
            .withSpacing(1)
            .build().apply {
                addComponent(Components.header().withText("Player"))
                addComponent(
                    Components.textBox(width)
                        .addParagraph("Health points: ${player.hp}")
                        .build(),
                )
            }
}
