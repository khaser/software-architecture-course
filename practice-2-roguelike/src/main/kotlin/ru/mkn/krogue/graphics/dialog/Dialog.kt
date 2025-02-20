package ru.mkn.krogue.graphics.dialog

import org.hexworks.zircon.api.builder.component.ModalBuilder
import org.hexworks.zircon.api.component.Container
import org.hexworks.zircon.api.component.modal.Modal
import org.hexworks.zircon.api.component.modal.ModalFragment
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.internal.component.modal.EmptyModalResult
import ru.mkn.krogue.graphics.ViewConfig

abstract class Dialog(
    private val screen: Screen,
    withClose: Boolean = true,
) : ModalFragment<EmptyModalResult> {
    abstract val container: Container

    final override val root: Modal<EmptyModalResult> by lazy {
        ModalBuilder.newBuilder<EmptyModalResult>()
            .withComponent(container)
            .withParentSize(screen.size)
            .withCenteredDialog(true)
            .build().also {
                if (withClose) {
                    container.addFragment(CloseButton(it, container))
                }
                container.theme = ViewConfig.theme
            }
    }
}
