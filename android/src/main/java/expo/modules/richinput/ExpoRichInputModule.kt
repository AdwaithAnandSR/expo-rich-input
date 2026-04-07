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

            Prop("id") {
                view: RichInputView, id: Int ->
                view.viewId = id
            }

            // 🔥 THIS IS THE KEY FIX
            OnViewDidUpdateProps {
                view ->
                view.onEditEvent = {
                    payload ->
                    this@ExpoRichInputModule.sendEvent("onEditEvent", payload)
                }

                view.onKeyboardAction = {
                    payload ->
                    this@ExpoRichInputModule.sendEvent("onKeyboardAction", payload)
                }

                view.onSelectionChange = {
                    payload ->
                    this@ExpoRichInputModule.sendEvent("onSelectionChange", payload)
                }
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