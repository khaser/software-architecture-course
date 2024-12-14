package ru.mkn.krogue

import org.hexworks.zircon.api.SwingApplications
import ru.mkn.krogue.graphics.ViewConfig
import ru.mkn.krogue.graphics.view.StartView
import ru.mkn.krogue.model.GameContext
import ru.mkn.krogue.model.GameController

fun main(args: Array<String>) {
    val gameContext = GameContext.newFromConfig()
    val gameController = GameController(gameContext)

    val grid = SwingApplications.startTileGrid(ViewConfig.appConfig())
    StartView(gameController, grid).dock()
}
