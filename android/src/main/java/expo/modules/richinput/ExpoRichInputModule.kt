package expo.modules.richinput

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

class ExpoRichInputModule : Module() {
    override fun definition() = ModuleDefinition {
        Name("ExpoRichInput")

        View(RichInputView::class) {

            Events("onEditEvent", "onKeyboardAction")

            AsyncFunction("focus") {
                view: RichInputView ->
                view.focusInput()
            }

            AsyncFunction("blur") {
                view: RichInputView ->
                view.blurInput()
            }

            // Wire up event dispatchers into the view
            OnViewDidUpdateProps {
                view ->
                view.onEditEvent = {
                    payload ->
                    view.dispatchEvent("onEditEvent", payload)
                }
                view.onKeyboardAction = {
                    payload ->
                    view.dispatchEvent("onKeyboardAction", payload)
                }
            }
        }
    }
}