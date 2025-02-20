package ru.mkn.krogue.graphics.dialog

import org.hexworks.zircon.api.ComponentDecorations
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.builder.component.ModalBuilder
import org.hexworks.zircon.api.component.ComponentAlignment
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.Processed
import org.hexworks.zircon.internal.component.modal.EmptyModalResult
import ru.mkn.krogue.graphics.ViewConfig
import ru.mkn.krogue.graphics.fragment.InventoryFragment
import ru.mkn.krogue.graphics.view.PlayView
import ru.mkn.krogue.model.game.Controller
import ru.mkn.krogue.model.player.Inventory

class InventoryDialog(controller: Controller, inventory: Inventory, screen: Screen, playView: PlayView) {
    init {
        val panel =
            Components.panel()
                .withSize(ViewConfig.Dialog.size)
                .withDecorations(ComponentDecorations.box(title = "Inventory"), ComponentDecorations.shadow())
                .build()

        val fragment =
            InventoryFragment(
                inventory = inventory,
                width = ViewConfig.Dialog.size.width - 3,
                onDrop = {
                    val status = controller.playerDropItem(it)
                    playView.update(status)
                },
                onEquip = {
                    val (oldItem, status) = controller.playerEquipItem(it)
                    playView.update(status)
                    oldItem
                },
            )

        panel.addFragment(fragment)

        val modal =
            ModalBuilder.newBuilder<EmptyModalResult>()
                .withParentSize(screen.size)
                .withComponent(panel)
                .withCenteredDialog(true)
                .build()

        panel.addComponent(
            Components.button()
                .withText("Close")
                .withAlignmentWithin(panel, ComponentAlignment.BOTTOM_LEFT)
                .build().apply {
                    onActivated {
                        modal.close(EmptyModalResult)
                        Processed
                    }
                },
        )

        modal.theme = ViewConfig.theme
        screen.openModal(modal)
    }
}
