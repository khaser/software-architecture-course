package ru.mkn.krogue

import org.hexworks.zircon.api.SwingApplications
import ru.mkn.krogue.graphics.ViewConfig
import ru.mkn.krogue.graphics.view.StartView
import ru.mkn.krogue.model.game.Controller

fun main(args: Array<String>) {
    val controller = Controller()

    val grid = SwingApplications.startTileGrid(ViewConfig.appConfig())
    StartView(controller, grid).dock()
}
