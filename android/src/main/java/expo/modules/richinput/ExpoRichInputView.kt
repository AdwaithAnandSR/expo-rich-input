package expo.modules.richinput

import android.content.Context
import android.os.Build
import android.text.InputType
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.BaseInputConnection
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputMethodManager
import expo.modules.kotlin.AppContext
import expo.modules.kotlin.views.ExpoView

class RichInputView(context: Context, appContext: AppContext) : ExpoView(context, appContext) {

  // Events dispatched to JS
  var onEditEvent: ((Map<String, Any>) -> Unit)? = null
  var onKeyboardAction: ((Map<String, Any>) -> Unit)? = null

  init {
    // View must be focusable to receive InputConnection
    isFocusable = true
    isFocusableInTouchMode = true
  }

  // MARK: InputConnection — this is where all text input flows through
  override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection {
    outAttrs.inputType = InputType.TYPE_CLASS_TEXT or
      InputType.TYPE_TEXT_FLAG_MULTI_LINE or
      InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS

    outAttrs.imeOptions = EditorInfo.IME_FLAG_NO_FULLSCREEN or
      EditorInfo.IME_ACTION_NONE

    // Disable autocorrect suggestions bar
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      outAttrs.initialCapsMode = 0
    }

    return EditorInputConnection(this)
  }

  override fun onCheckIsTextEditor(): Boolean = true

  // MARK: Hardware key events (physical keyboard, back key etc.)
  override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
    if (event.isCtrlPressed) {
      when (keyCode) {
        KeyEvent.KEYCODE_Z -> {
          if (event.isShiftPressed) {
            onKeyboardAction?.invoke(mapOf("action" to "redo"))
          } else {
            onKeyboardAction?.invoke(mapOf("action" to "undo"))
          }
          return true
        }
        KeyEvent.KEYCODE_SLASH -> {
          onKeyboardAction?.invoke(mapOf("action" to "toggleComment"))
          return true
        }
      }
    }
    return super.onKeyDown(keyCode, event)
  }

  // MARK: Focus control called from JS
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

  // MARK: InputConnection inner class
  inner class EditorInputConnection(view: View) : BaseInputConnection(view, false) {

    // Regular text commit — typing, paste, swipe keyboard word
    override fun commitText(text: CharSequence?, newCursorPosition: Int): Boolean {
      val str = text?.toString() ?: return false
      onEditEvent?.invoke(mapOf(
        "type" to "insert",
        "text" to str
      ))
      return true
    }

    // IME composing (Gboard swipe intermediate states, CJK input)
    override fun setComposingText(text: CharSequence?, newCursorPosition: Int): Boolean {
      val str = text?.toString() ?: ""
      onEditEvent?.invoke(mapOf(
        "type" to "compose",
        "text" to str
      ))
      return true
    }

    // IME composition confirmed
    override fun finishComposingText(): Boolean {
      onEditEvent?.invoke(mapOf(
        "type" to "composeCommit"
      ))
      return true
    }

    // Backspace — beforeLength is almost always 1
    override fun deleteSurroundingText(beforeLength: Int, afterLength: Int): Boolean {
      if (beforeLength > 0) {
        onEditEvent?.invoke(mapOf(
          "type" to "delete",
          "count" to beforeLength
        ))
      }
      return true
    }

    // Disable autocorrect suggestions — return null to tell the OS
    // there is no surrounding text context to work with
    override fun getExtractedText(request: android.view.inputmethod.ExtractedTextRequest?, flags: Int)
      : android.view.inputmethod.ExtractedText? = null

    override fun getSurroundingText(beforeLength: Int, afterLength: Int, flags: Int)
      : android.view.inputmethod.SurroundingText? = null
  }
}
