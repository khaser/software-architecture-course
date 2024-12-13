package ru.mkn.krogue

import org.hexworks.zircon.api.SwingApplications
import ru.mkn.krogue.graphics.ViewConfig
import ru.mkn.krogue.graphics.view.StartView
fun main(args: Array<String>) {

    val grid = SwingApplications.startTileGrid(ViewConfig.appConfig())
    StartView(grid).dock()

}