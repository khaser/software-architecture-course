package ru.mkn.krogue.graphics.fragment

import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment

class PlayerStatsFragment(
    width: Int,
) : Fragment {
    val mc =
        Components.header()
            .withText("")
            .withSize(width, 1)
            .build()

    override val root =
        Components.vbox()
            .withSize(width, 30)
            .withSpacing(1)
            .build().apply {
                addComponent(Components.header().withText("Player"))
                addComponent(mc)
            }
}
