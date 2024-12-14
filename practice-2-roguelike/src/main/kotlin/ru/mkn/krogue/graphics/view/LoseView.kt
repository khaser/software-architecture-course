package ru.mkn.krogue.graphics.view

import org.hexworks.zircon.api.ComponentDecorations
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.view.base.BaseView
import ru.mkn.krogue.graphics.ViewConfig
import ru.mkn.krogue.model.GameController
import kotlin.system.exitProcess

class LoseView(
    private val gameController: GameController,
    private val grid: TileGrid,
    private val causeOfDeath: String,
) : BaseView(grid, ViewConfig.theme) {
    init {
        val msg = "Game Over"
        val header =
            Components.textBox(30)
                .addHeader(msg)
                .addParagraph(causeOfDeath)
                .addNewLine()
                .withAlignmentWithin(screen, ComponentAlignment.CENTER)
                .build()
        val restartButton =
            Components.button()
                .withAlignmentAround(header, ComponentAlignment.BOTTOM_LEFT)
                .withText("Restart")
                .withDecorations(ComponentDecorations.box(BoxType.SINGLE))
                .build()
        val exitButton =
            Components.button()
                .withAlignmentAround(header, ComponentAlignment.BOTTOM_RIGHT)
                .withText("Quit")
                .withDecorations(ComponentDecorations.box(BoxType.SINGLE))
                .build()

        restartButton.onActivated {
            replaceWith(PlayView(gameController, grid))
        }

        exitButton.onActivated {
            exitProcess(0)
        }

        screen.addComponent(header)
        screen.addComponent(restartButton)
        screen.addComponent(exitButton)
    }
}
