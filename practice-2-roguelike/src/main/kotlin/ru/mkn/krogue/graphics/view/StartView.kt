package ru.mkn.krogue.graphics.view

import org.hexworks.zircon.api.ComponentDecorations.box
import org.hexworks.zircon.api.ComponentDecorations.shadow
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.view.base.BaseView
import ru.mkn.krogue.graphics.ViewConfig

class StartView(
    private val grid: TileGrid,
) : BaseView(grid, ViewConfig.theme) {
    init {
        val msg = "Welcome to Krogue."

        val header =
            Components.textBox(contentWidth = msg.length)
                .addHeader(msg)
                .addNewLine()
                .withAlignmentWithin(screen, ComponentAlignment.CENTER)
                .build()

        val startButton =
            Components.button()
                .withAlignmentAround(header, ComponentAlignment.BOTTOM_CENTER)
                .withText("Play!")
                .withDecorations(box(), shadow())
                .build()

        startButton.onActivated {
            replaceWith(PlayView(TODO(), grid))
        }

        screen.addComponents(header, startButton)
    }
}
