package ru.mkn.krogue.graphics.fragment

import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.component.VBox
import ru.mkn.krogue.graphics.ViewConfig
import ru.mkn.krogue.model.Item
import ru.mkn.krogue.model.player.Inventory

class InventoryFragment(
    inventory: Inventory,
    width: Int,
    private val onDrop: (Item) -> Unit,
    private val onEquip: (Item) -> Unit,
) : Fragment {
    override val root =
        Components.vbox()
            .withSize(width, inventory.items.size + 1)
            .build().apply {
                val list = this

                addComponent(
                    Components.hbox()
                        .withSpacing(1)
                        .withSize(width, 1)
                        .build().apply {
                            addComponent(Components.label().withText("").withSize(1, 1))
                            addComponent(Components.header().withText("Name").withSize(NAME_COLUMN_WIDTH, 1))
                            addComponent(Components.header().withText("Actions").withSize(ACTIONS_COLUMN_WIDTH, 1))
                        },
                )
                inventory.items.forEach { item ->
                    addRow(width, item, list)
                }
            }

    private fun addRow(
        width: Int,
        item: Item,
        list: VBox,
    ) {
        val row = InventoryRowFragment(width, item)
        list.addFragment(row).apply {
            row.dropButton.onActivated {
                detach()
                onDrop(item)
            }
            row.equipButton.onActivated {
                detach()
                onEquip(item)
            }
        }
        list.theme = ViewConfig.theme
    }

    companion object {
        const val NAME_COLUMN_WIDTH = 15
        const val ACTIONS_COLUMN_WIDTH = 10
    }
}
