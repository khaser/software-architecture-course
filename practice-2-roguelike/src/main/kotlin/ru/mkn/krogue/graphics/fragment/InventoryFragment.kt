package ru.mkn.krogue.graphics.fragment

import org.hexworks.cobalt.datatypes.Maybe
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import org.hexworks.zircon.api.component.VBox
import org.hexworks.zircon.api.data.Tile
import ru.mkn.krogue.graphics.ViewConfig
import ru.mkn.krogue.graphics.tile.DaggerTile
import ru.mkn.krogue.graphics.tile.JacketTile
import ru.mkn.krogue.model.Armor
import ru.mkn.krogue.model.GameState
import ru.mkn.krogue.model.Item
import ru.mkn.krogue.model.Weapon
import ru.mkn.krogue.model.player.Inventory

class InventoryFragment(
    inventory: Inventory,
    width: Int,
    private val onDrop: (Item) -> GameState,
    private val onEquip: (Item) -> Maybe<Item>,
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
                onEquip(item).map { oldItem ->
                    detach()
                    addRow(width, oldItem, list)
                }
            }
        }
        list.theme = ViewConfig.theme
    }

    companion object {
        const val NAME_COLUMN_WIDTH = 15
        const val ACTIONS_COLUMN_WIDTH = 10

        val itemToTile: Map<Item, Tile> =
            mapOf(
                Weapon.Dagger to DaggerTile,
                Armor.Jacket to JacketTile,
            )
    }
}
