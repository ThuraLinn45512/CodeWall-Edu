package com.lesson.codewalledu.src.ui.views.auth.otp_code
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.lesson.codewalledu.R

class OtpTextWatcher(
    private val currentEditText: EditText,
    private val nextEditText: EditText?
) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // Not needed
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (s?.length == 1 && nextEditText != null) {
            nextEditText.requestFocus() // Move focus to the next EditText
        }
    }

    override fun afterTextChanged(s: Editable?) {
        if (s?.isEmpty() == true && currentEditText.id != R.id.edit_text_otp_1) {
            currentEditText.focusSearch(View.FOCUS_LEFT).requestFocus()
        }
    }
}