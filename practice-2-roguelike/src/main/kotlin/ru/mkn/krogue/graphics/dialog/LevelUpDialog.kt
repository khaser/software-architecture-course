package ru.mkn.krogue.graphics.dialog

import org.hexworks.zircon.api.ComponentDecorations
import org.hexworks.zircon.api.Components
import org.hexworks.zircon.api.graphics.BoxType
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.internal.component.modal.EmptyModalResult
import ru.mkn.krogue.model.player.LevelUPStat
import ru.mkn.krogue.model.player.Player

class LevelUpDialog(
    screen: Screen,
    player: Player,
    onGainLevel: (LevelUPStat) -> Unit,
) : Dialog(screen, false) {
    override val container =
        Components.vbox()
            .withDecorations(ComponentDecorations.box(title = "Ding!", boxType = BoxType.TOP_BOTTOM_DOUBLE))
            .withSize(30, 15)
            .build().apply {
                addComponent(
                    Components.textBox(27)
                        .addHeader("Congratulations, you leveled up!")
                        .addParagraph("Pick an improvement from the options below:"),
                )

                addComponent(
                    Components.button()
                        .withText("Max HP")
                        .build().apply {
                            onActivated {
                                onGainLevel(LevelUPStat.HP)
                                root.close(EmptyModalResult)
                            }
                        },
                )

                addComponent(
                    Components.button()
                        .withText("Attack")
                        .build().apply {
                            onActivated {
                                onGainLevel(LevelUPStat.ATK)
                                root.close(EmptyModalResult)
                            }
                        },
                )

                addComponent(
                    Components.button()
                        .withText("Defense")
                        .build().apply {
                            onActivated {
                                onGainLevel(LevelUPStat.DEF)
                                root.close(EmptyModalResult)
                            }
                        },
                )
            }
}
