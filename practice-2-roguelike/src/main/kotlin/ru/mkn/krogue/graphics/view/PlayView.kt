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
import ru.mkn.krogue.graphics.ViewConfig
import ru.mkn.krogue.graphics.World
import ru.mkn.krogue.graphics.dialog.InventoryDialog
import ru.mkn.krogue.graphics.fragment.PlayerStatsFragment
import ru.mkn.krogue.graphics.tile.FloorTile
import ru.mkn.krogue.model.GameContext
import ru.mkn.krogue.model.GameController
import ru.mkn.krogue.model.GameState
import ru.mkn.krogue.model.map.Direction

class PlayView(
    gameController: GameController,
    private val grid: TileGrid,
    world: World = World(gameController.context),
) : BaseView(grid, ViewConfig.theme) {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val context = gameController.context

    private val sidebar =
        Components.panel()
            .withSize(ViewConfig.SideBar.size)
            .withDecorations(ComponentDecorations.box())
            .build()

    val playerStatsFragment =
        PlayerStatsFragment(
            player = context.player,
            width = sidebar.contentSize.width,
        )

    fun checkGame(status: GameState) {
        if (status == GameState.OVER) {
            replaceWith(LoseView(GameController(GameContext.newFromConfig()), grid, "Skill issue"))
        }
    }

    init {
        sidebar.addFragment(playerStatsFragment)

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
            val status =
                when (event.code) {
                    KeyCode.KEY_W, KeyCode.KEY_A, KeyCode.KEY_S, KeyCode.KEY_D -> {
                        val direction =
                            when (event.code) {
                                KeyCode.KEY_W -> Direction.UP
                                KeyCode.KEY_A -> Direction.LEFT
                                KeyCode.KEY_S -> Direction.DOWN
                                KeyCode.KEY_D -> Direction.RIGHT
                                else -> throw IllegalStateException("Unreachable")
                            }
                        gameController.movePlayer(direction)
                    }
                    KeyCode.KEY_I -> {
                        InventoryDialog(gameController, context.player.inventory, screen, this)
                        GameState.IN_PROGRESS
                    }
                    KeyCode.KEY_P -> {
                        gameController.playerPickItem()
                    }
                    else -> {
                        GameState.IN_PROGRESS
                    }
                }
            checkGame(status)
            playerStatsFragment.updateStats()
            world.update()
            Processed
        }

        screen.addComponents(sidebar, logArea, gameComponent)
        // initial update
        world.update()
    }
}
