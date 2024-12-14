package ru.mkn.krogue.graphics.view

import org.hexworks.cobalt.databinding.api.extension.toProperty
import org.hexworks.zircon.api.ComponentDecorations
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.game.ProjectionMode
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.uievent.KeyboardEventType
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.api.view.base.BaseView
import org.hexworks.zircon.internal.game.impl.GameAreaComponentRenderer
import ru.mkn.krogue.graphics.FloorTile
import ru.mkn.krogue.graphics.ViewConfig
import ru.mkn.krogue.graphics.World
import ru.mkn.krogue.model.GameContext

class PlayView(
    gameContext: GameContext,
    grid: TileGrid,
    world: World = World(gameContext),
) : BaseView(grid, ViewConfig.theme) {
    init {
        val sidebar =
            Components.panel()
                .withSize(ViewConfig.SideBar.size)
                .withDecorations(ComponentDecorations.box())
                .build()

        val logArea =
            Components.logArea()
                .withDecorations(ComponentDecorations.box(title = "Log"))
                .withSize(ViewConfig.Log.size)
                .withAlignmentWithin(screen, ComponentAlignment.BOTTOM_RIGHT)
                .build()

        val gameComponent =
            Components.panel()
                .withSize(world.visibleSize.to2DSize())
                .withComponentRenderer(
                    GameAreaComponentRenderer(
                        gameArea = world,
                        projectionMode = ProjectionMode.TOP_DOWN.toProperty(),
                        fillerTile = FloorTile,
                    ),
                )
                .withAlignmentWithin(screen, ComponentAlignment.TOP_RIGHT)
                .build()

        screen.handleKeyboardEvents(KeyboardEventType.KEY_PRESSED) { event, _ ->
            // TODO(kmitkin): send command to model
            world.update()
            Processed
        }

        screen.addComponents(sidebar, logArea, gameComponent)
    }
}
