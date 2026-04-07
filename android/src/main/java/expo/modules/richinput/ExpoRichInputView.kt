package expo.modules.richinput

import android.content.Context
import android.os.Build
import android.text.InputType
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.*
import expo.modules.kotlin.AppContext
import expo.modules.kotlin.views.ExpoView

class RichInputView(context: Context, appContext: AppContext) : ExpoView(context, appContext) {

    var viewId: Int = -1

    // ✅ CALLBACKS (IMPORTANT)
    var onEditEvent: ((Map<String, Any>) -> Unit)? = null
    var onKeyboardAction: ((Map<String, Any>) -> Unit)? = null
    var onSelectionChange: ((Map<String, Any>) -> Unit)? = null

    private var cursorPosition = 0
    private var selectionStart = 0
    private var selectionEnd = 0

    init {
        isFocusable = true
        isFocusableInTouchMode = true
    }

    override fun onCheckIsTextEditor(): Boolean = true

    override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection {
        outAttrs.inputType = InputType.TYPE_CLASS_TEXT or
            InputType.TYPE_TEXT_FLAG_MULTI_LINE or
            InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS

        outAttrs.imeOptions = EditorInfo.IME_FLAG_NO_FULLSCREEN or
            EditorInfo.IME_ACTION_NONE

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            outAttrs.initialCapsMode = 0
        }

        return EditorInputConnection(this)
    }

    // 🔥 Hardware keyboard
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.isCtrlPressed) {
            val action = when (keyCode) {
                KeyEvent.KEYCODE_Z -> if (event.isShiftPressed) "redo" else "undo"
                KeyEvent.KEYCODE_A -> "selectAll"
                KeyEvent.KEYCODE_C -> "copy"
                KeyEvent.KEYCODE_V -> "paste"
                KeyEvent.KEYCODE_X -> "cut"
                KeyEvent.KEYCODE_SLASH -> "toggleComment"
                else -> null
            }

            action?.let {
                onKeyboardAction?.invoke(mapOf(
                    "id" to viewId,
                    "action" to it
                ))
                return true
            }
        }

        return super.onKeyDown(keyCode, event)
    }

    fun focusInput() {
        requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }

    fun blurInput() {
        clearFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    inner class EditorInputConnection(view: View) : BaseInputConnection(view, false) {

        override fun commitText(text: CharSequence?, newCursorPosition: Int): Boolean {
            val str = text?.toString() ?: return false

            onEditEvent?.invoke(mapOf(
                "id" to viewId,
                "type" to "insert",
                "text" to str,
                "cursor" to cursorPosition
            ))

            cursorPosition += str.length
            selectionStart = cursorPosition
            selectionEnd = cursorPosition

            return true
        }

        override fun deleteSurroundingText(beforeLength: Int, afterLength: Int): Boolean {
            if (beforeLength > 0) {
                val deleteCount = minOf(beforeLength, cursorPosition)

                onEditEvent?.invoke(mapOf(
                    "id" to viewId,
                    "type" to "delete",
                    "count" to deleteCount,
                    "cursor" to cursorPosition
                ))

                cursorPosition -= deleteCount
            }
            return true
        }

        override fun setSelection(start: Int, end: Int): Boolean {
            selectionStart = start
            selectionEnd = end
            cursorPosition = end

            onSelectionChange?.invoke(mapOf(
                "id" to viewId,
                "start" to start,
                "end" to end
            ))

            return true
        }
    }
}