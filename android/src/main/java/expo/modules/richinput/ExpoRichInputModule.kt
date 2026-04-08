package expo.modules.richinput

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

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
            }

            AsyncFunction("blur") {
                view: RichInputView ->
                view.blurInput()
            }
        }
    }
}