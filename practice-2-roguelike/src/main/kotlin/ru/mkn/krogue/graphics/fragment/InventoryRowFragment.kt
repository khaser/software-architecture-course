package ru.mkn.krogue.graphics.fragment

import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.component.Fragment
import ru.mkn.krogue.model.Item

class InventoryRowFragment(width: Int, item: Item) : Fragment {
    val dropButton =
        Components.button()
            .withDecorations()
            .withText("Drop")
            .build()

    val equipButton =
        Components.button()
            .withDecorations()
            .withText("Equip")
            .build()

    override val root =
        Components.hbox()
            .withSpacing(1)
            .withSize(width, 1)
            .build().apply {
                addComponent(
                    Components.icon()
                        .withIcon(InventoryFragment.itemToTile[item] ?: throw IllegalArgumentException("Unexpected $item")),
                )
                addComponent(
                    Components.label()
                        .withSize(InventoryFragment.NAME_COLUMN_WIDTH, 1)
                        .withText(item.name),
                )
                addComponent(dropButton)
                addComponent(equipButton)
            }
}
