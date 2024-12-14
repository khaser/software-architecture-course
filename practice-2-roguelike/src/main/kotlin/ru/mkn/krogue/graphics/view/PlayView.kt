package ru.mkn.krogue.graphics.view

import org.hexworks.cobalt.databinding.api.extension.toProperty
import org.hexworks.cobalt.logging.api.LoggerFactory
import org.hexworks.zircon.api.ComponentDecorations
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.game.ProjectionMode
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.KeyboardEventType
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.api.view.base.BaseView
import org.hexworks.zircon.internal.game.impl.GameAreaComponentRenderer
import ru.mkn.krogue.graphics.FloorTile
import ru.mkn.krogue.graphics.ViewConfig
import ru.mkn.krogue.graphics.World
import ru.mkn.krogue.graphics.fragment.PlayerStatsFragment
import ru.mkn.krogue.model.GameController
import ru.mkn.krogue.model.GameState
import ru.mkn.krogue.model.map.Direction

class PlayView(
    gameController: GameController,
    grid: TileGrid,
    world: World = World(gameController.context),
) : BaseView(grid, ViewConfig.theme) {
    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        val sidebar =
            Components.panel()
                .withSize(ViewConfig.SideBar.size)
                .withDecorations(ComponentDecorations.box())
                .build()

        sidebar.addFragment(
            PlayerStatsFragment(
                width = sidebar.contentSize.width,
                player = gameController.context.player,
            ),
        )

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
            logger.info("Receiving player input: $event")
            val direction =
                when (event.code) {
                    KeyCode.KEY_W -> Direction.UP
                    KeyCode.KEY_A -> Direction.LEFT
                    KeyCode.KEY_S -> Direction.DOWN
                    KeyCode.KEY_D -> Direction.RIGHT
                    else -> null
                }
            if (direction != null) {
                val gameStatus = gameController.movePlayer(direction)
                if (gameStatus == GameState.OVER) {
                    replaceWith(LoseView(gameController, grid, "Skill issue"))
                }
                println("Player hp: ${gameController.context.player.hp}")
                world.update()
            } else {
//                TODO("not implemented")
            }
            Processed
        }

        screen.addComponents(sidebar, logArea, gameComponent)
        // initial update
        world.update()
    }
}
