package com.xvantage.rental.utils

import android.app.Activity
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout

object ValidationUtils {
    private const val TAG = "ValidationUtils"

    // Regex patterns for validation
    const val validate_phone = "^[0,2-9][0-9]{9}$" // for Canada only

    // const val validate_postalcode_ca = "[ABCEGHJKLMNPRSTVXY|abceghjklmnprstvxy][0-9][ABCEGHJKLMNPRSTVWXYZ|abceghjklmnprstvwxyz] ?[0-9][ABCEGHJKLMNPRSTVWXYZ|abceghjklmnprstvwxyz][0-9]"
    const val validate_postalcode_ca =
        "(^[ABCEGHJKLMNPRSTVXY]\\d[ABCEGHJKLMNPRSTVWXYZ]$)|(^[ABCEGHJKLMNPRSTVXY]\\d[ABCEGHJKLMNPRSTVWXYZ]( )?\\d[ABCEGHJKLMNPRSTVWXYZ]\\d$)|(^\\d{5}(-\\d{4})?$)|(^\\d{3,6}?$)"
    const val validate_postalcode_us = "^\\d{5}([ \\-]\\d{4})?"

    // Validate phone number
    fun isValidPhone(value: String): Boolean {
        return Patterns.PHONE.matcher(value).matches()
    }

    // Validate postal code with a custom regex pattern
    fun isValidPostalCode(value: String, validatePattern: String): Boolean {
        return try {
            val pattern = Regex(validatePattern)
            pattern.matches(value)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // Set max length for EditText or TextView
    fun setMaxLength(view: View, maxLength: Int) {
        try {
            when (view) {
                is EditText -> view.filters = arrayOf(InputFilter.LengthFilter(maxLength))
                is TextView -> view.filters = arrayOf(InputFilter.LengthFilter(maxLength))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Validate email address
    fun isValidEmail(value: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(value).matches()
    }

    // Validate password (must contain at least one digit, one uppercase letter, one lowercase letter, and one special character)
    fun isValidPassword(value: String): Boolean {
        return value.length >= 8 &&
                value.contains(Regex(".*\\d.*")) &&
                value.contains(Regex(".*[A-Z].*")) &&
                value.contains(Regex(".*[a-z].*")) &&
                value.contains(Regex(".*[#?!@$%^&*-].*"))
    }

    // EmptyTextWatcher for EditText
    class EmptyTextWatcher(
        private val textInputLayout: TextInputLayout,
        private val editText: EditText,
        private val errorMessage: String
    ) : TextWatcher {
        override fun beforeTextChanged(
            charSequence: CharSequence?,
            start: Int,
            before: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(
            charSequence: CharSequence?,
            start: Int,
            before: Int,
            after: Int
        ) {
        }

        override fun afterTextChanged(editable: Editable?) {
            if (TextUtils.isEmpty(editText.text.toString().trim())) {
                textInputLayout.error = errorMessage
            } else {
                textInputLayout.isErrorEnabled = false
            }
        }
    }

    // EmptyTextWatcher for TextView
    class EmptyTextViewTextWatcher(
        private val textInputLayout: TextInputLayout,
        private val textView: TextView,
        private val errorMessage: String
    ) : TextWatcher {
        override fun beforeTextChanged(
            charSequence: CharSequence?,
            start: Int,
            before: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(
            charSequence: CharSequence?,
            start: Int,
            before: Int,
            after: Int
        ) {
        }

        override fun afterTextChanged(editable: Editable?) {
            if (TextUtils.isEmpty(textView.text.toString().trim())) {
                textInputLayout.error = errorMessage
            } else {
                textInputLayout.isErrorEnabled = false
            }
        }
    }

    /*// Email validation text watcher
    class EmailTextWatcher(private val activity: Activity, private val textInputLayout: TextInputLayout, private val editText: EditText) : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence?, start: Int, before: Int, after: Int) {}

        override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, after: Int) {}

        override fun afterTextChanged(editable: Editable?) {
            if (TextUtils.isEmpty(editText.text.toString().trim())) {
                textInputLayout.error = activity.resources.getString(R.string.error_enter_email)
            } else {
                textInputLayout.isErrorEnabled = false
            }
        }
    }*/

    /*// Phone number validation text watcher
    class PhoneTextWatcher(private val activity: Activity, private val textInputLayout: TextInputLayout, private val editText: EditText) : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence?, start: Int, before: Int, after: Int) {}

        override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, after: Int) {}

        override fun afterTextChanged(editable: Editable?) {
            if (!TextUtils.isEmpty(editText.text.toString().trim()) &&
                editText.text.toString().trim().substring(0, 1) == "1") {
                textInputLayout.error = activity.resources.getString(R.string.error_enter_valid_mobile_no)
            } else if (!isValidPhone(editText.text.toString().trim())) {
                textInputLayout.error = activity.resources.getString(R.string.error_enter_valid_mobile_no)
            } else {
                textInputLayout.isErrorEnabled = false
            }
        }
    }
*/
    // Postal code validation text watcher
    class PostalCodeLengthTextWatcher(
        private val activity: Activity,
        private val editText: EditText
    ) : TextWatcher {
        private val space = ' '

        override fun beforeTextChanged(
            charSequence: CharSequence?,
            start: Int,
            before: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(
            charSequence: CharSequence?,
            start: Int,
            before: Int,
            after: Int
        ) {
        }

        override fun afterTextChanged(s: Editable?) {
            try {
                if (s != null && s.isNotEmpty()) {
                    // Remove spacing char
                    if (s.length % 4 == 0 && !Character.isDigit(s[0])) {
                        val c = s[s.length - 1]
                        if (space == c) {
                            s.delete(s.length - 1, s.length)
                        }
                    }
                    // Insert char where needed
                    if (s.length > 0 && s.length % 4 == 0 && !Character.isDigit(s[0])) {
                        if (TextUtils.split(s.toString(), space.toString()).size <= 3) {
                            s.insert(s.length - 1, space.toString())
                        }
                    }
                    if (s.toString().contains(space.toString())) {
                        setMaxLength(editText, 7)
                    } else {
                        setMaxLength(editText, 7)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
