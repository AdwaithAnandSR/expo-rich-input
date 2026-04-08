package expo.modules.richinput

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
expo.modules.kotlin.queues.Queues

class ExpoRichInputModule : Module() {
    override fun definition() = ModuleDefinition {

        Name("ExpoRichInput")

        View(RichInputView::class) {

            Events(
                "onEditEvent",
                "onKeyboardAction",
                "onSelectionChange"
            )

            Prop("editable") {
                view: RichInputView, value: Boolean ->
                view.isEnabled = value
            }

            AsyncFunction("focus") {
                view: RichInputView ->
                view.focusInput()
            }.runOnQueue(Queues.MAIN)

            AsyncFunction("blur") {
                view: RichInputView ->
                view.blurInput()
            }.runOnQueue(Queues.MAIN)
        }
    }
}