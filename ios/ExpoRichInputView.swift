import ExpoModulesCore
import UIKit

class RichInputView: ExpoView, UIKeyInput {

    // MARK: Events
    let onEditEvent = EventDispatcher()
    let onKeyboardAction = EventDispatcher()

    // MARK: UIKeyInput — required
    var hasText: Bool {
        true
    } // always true so backspace keeps firing

    func insertText(_ text: String) {
        // Tab key comes as "\t"
        // Newline comes as "\n"
        onEditEvent([
            "type": "insert",
            "text": text
        ])
    }

    func deleteBackward() {
        onEditEvent([
            "type": "delete",
            "count": 1
        ])
    }

    // MARK: UIResponder — keyboard traits
    override var canBecomeFirstResponder: Bool {
        true
    }

    override var autocorrectionType: UITextAutocorrectionType {
        get {
            .no
        }
        set {}
    }

    override var autocapitalizationType: UITextAutocapitalizationType {
        get {
            .none
        }
        set {}
    }

    override var spellCheckingType: UITextSpellCheckingType {
        get {
            .no
        }
        set {}
    }

    override var keyboardType: UIKeyboardType {
        get {
            .asciiCapable
        }
        set {}
    }

    override var returnKeyType: UIReturnKeyType {
        get {
            .default
            }
            set {}
        }

        override var smartQuotesType: UITextSmartQuotesType {
            get {
                .no
            }
            set {}
        }

        // MARK: Hardware keyboard shortcuts (iPad + external keyboard)
        override var keyCommands: [UIKeyCommand]? {
            return [
                UIKeyCommand(
                    input: "z",
                    modifierFlags: .command,
                    action: #selector(handleUndo)
                ),
                UIKeyCommand(
                    input: "z",
                    modifierFlags: [.command, .shift],
                    action: #selector(handleRedo)
                ),
                UIKeyCommand(
                    input: "/",
                    modifierFlags: .command,
                    action: #selector(handleToggleComment)
                ),
            ]
        }

        @objc func handleUndo() {
            onKeyboardAction(["action": "undo"])
        }

        @objc func handleRedo() {
            onKeyboardAction(["action": "redo"])
        }

        @objc func handleToggleComment() {
            onKeyboardAction(["action": "toggleComment"])
        }

        // MARK: Focus control (called from JS)
        func focusInput() {
            becomeFirstResponder()
        }

        func blurInput() {
            resignFirstResponder()
        }
    }