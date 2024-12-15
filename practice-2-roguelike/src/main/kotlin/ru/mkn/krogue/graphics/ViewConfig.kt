package ru.mkn.krogue.graphics

import org.hexworks.zircon.api.ColorThemes
import org.hexworks.zircon.api.GraphicalTilesetResources
import org.hexworks.zircon.api.data.Size
import ru.mkn.krogue.model.Config

object ViewConfig {
    val tileset = GraphicalTilesetResources.nethack16x16()
    val theme = ColorThemes.linuxMintDark()

    val size: Size = Size.create(Config.mapSize.width + 18, Config.mapSize.height + 8)

    object SideBar {
        val size: Size = Size.create(18, ViewConfig.size.height)
    }

    object Log {
        val size: Size = Size.create(ViewConfig.size.width - SideBar.size.width, 8)
    }

    object GameArea {
        val size: Size = Size.create(ViewConfig.size.width - SideBar.size.width, ViewConfig.size.height - Log.size.height)
    }

    object World {
        val size: Size = Config.mapSize
    }

    object Dialog {
        val size: Size = Size.create(40, 15)
    }

    fun appConfig() =
        org.hexworks.zircon.api.application.AppConfig.newBuilder()
            .withDefaultGraphicalTileset(tileset)
            .withSize(size)
            .build()
}
