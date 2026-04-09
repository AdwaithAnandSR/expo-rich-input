package expo.modules.richinput

import android.content.Context
import android.text.InputType
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.*
import expo.modules.kotlin.AppContext
import expo.modules.kotlin.views.ExpoView
import expo.modules.kotlin.viewevent.EventDispatcher
import android.view.MotionEvent

class RichInputView(
    context: Context,
    appContext: AppContext
) : ExpoView(context, appContext) {

    // ✅ Corrected: Infer type from usage instead of explicit generic
    private val onEditEvent by EventDispatcher()
    private val onKeyboardAction by EventDispatcher()
    private val onSelectionChange by EventDispatcher()

    init {
        isFocusable = true
        isFocusableInTouchMode = true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            focusInput()
            performClick()
        }
        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    override fun onCheckIsTextEditor(): Boolean = true

    override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection {
        outAttrs.inputType =
            InputType.TYPE_CLASS_TEXT or
            InputType.TYPE_TEXT_FLAG_MULTI_LINE or
            InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS

        outAttrs.imeOptions =
            EditorInfo.IME_FLAG_NO_FULLSCREEN or
            EditorInfo.IME_ACTION_NONE

        outAttrs.initialCapsMode = 0

        return EditorInputConnection(this)
    }

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
                onKeyboardAction(mapOf("action" to it))
                return true
            }
        }

        return super.onKeyDown(keyCode, event)
    }

    fun focusInput() {
        requestFocus()
        post {
            val imm =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(this, InputMethodManager.SHOW_FORCED)
        }
    }

    fun blurInput() {
        clearFocus()
        val imm =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    inner class EditorInputConnection(view: View) :
        BaseInputConnection(view, false) {

        override fun commitText(text: CharSequence?, newCursorPosition: Int): Boolean {
            val str = text?.toString() ?: return false

            onEditEvent(
                mapOf(
                    "type" to "insert",
                    "text" to str
                )
            )

            return true
        }

        override fun setComposingText(
            text: CharSequence?,
            newCursorPosition: Int
        ): Boolean {
            val str = text?.toString() ?: ""

            onEditEvent(mapOf(
                "type" to "compose",
                "text" to str
            ))

            return true
        }

        override fun finishComposingText(): Boolean {
            onEditEvent(
                mapOf("type" to "composeCommit")
            )

            return true
        }

        override fun deleteSurroundingText(
            beforeLength: Int,
            afterLength: Int
        ): Boolean {
            if (beforeLength > 0) {
                onEditEvent(
                    mapOf(
                        "type" to "delete",
                        "count" to beforeLength
                    )
                )
            }
            return true
        }

        override fun setSelection(start: Int, end: Int): Boolean {
            onSelectionChange(
                mapOf(
                    "start" to start,
                    "end" to end
                )
            )
            return true
        }

        override fun performEditorAction(actionCode: Int): Boolean {
            val action = when (actionCode) {
                EditorInfo.IME_ACTION_DONE,
                EditorInfo.IME_ACTION_GO,
                EditorInfo.IME_ACTION_SEND,
                EditorInfo.IME_ACTION_NEXT -> "enter"
                else -> "unknown"
            }

            onKeyboardAction(
                mapOf("action" to action)
            )

            return true
        }

        override fun getExtractedText(
            request: ExtractedTextRequest?,
            flags: Int
        ): ExtractedText? = null

        override fun getSurroundingText(
            beforeLength: Int,
            afterLength: Int,
            flags: Int
        ): SurroundingText? = null
    }
}