import ExpoModulesCore

public class RichInputModule: Module {
    public func definition() -> ModuleDefinition {
        Name("ExpoRichInput")

        View(RichInputView.self) {

            // MARK: Events exposed to JS
            Events("onEditEvent", "onKeyboardAction")

            // MARK: Functions callable from JS
            // ref.current.focus()
            AsyncFunction("focus") {
                (view: RichInputView) in
                DispatchQueue.main.async {
                    view.focusInput()
                }
            }

            // ref.current.blur()
            AsyncFunction("blur") {
                (view: RichInputView) in
                DispatchQueue.main.async {
                    view.blurInput()
                }
            }
        }
    }
}