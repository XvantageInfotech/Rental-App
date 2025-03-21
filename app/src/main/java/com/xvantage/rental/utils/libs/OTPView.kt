package com.xvantage.rental.utils.libs

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import com.xvantage.rental.R
import com.xvantage.rental.databinding.OtpViewLayoutBinding
import java.util.Locale

/**
 * Project: Rental App By XV Team
 * Author: Mujammil x Vipul x XV Team
 * Date:  02/02/25
 * <p>
 * Licensed under the Apache License, Version 2.0. See LICENSE file for terms.
 */


class OTPView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    // region values
    // All
    private val itemCount: Int
    private val showCursor: Boolean
    private val underscoreCursor: Boolean
    private val customCursorDrawable: Drawable?
    private val inputType: Int
    private val importantForAutofillLocal: Int
    private val autofillHints: String?
    private var itemWidth: Int
    private var itemHeight: Int
    private val cursorColor: Int
    private val allCaps: Boolean
    private val marginBetween: Int
    private val isPassword: Boolean

    private val otpViewLayoutBinding: OtpViewLayoutBinding = OtpViewLayoutBinding.bind(
        LayoutInflater.from(context).inflate(R.layout.otp_view_layout, this, true)
    )

    // Default

    private val textSizeDefault: Int
    private val textColor: Int
    private val backgroundImage: Drawable?
    private val font: Typeface?

    // Highlighted

    private val highlightedTextSize: Int
    private val highlightedTextColor: Int
    private val highlightedBackgroundImage: Drawable?
    private val highlightedFont: Typeface?

    // Filled

    private val filledTextSize: Int
    private val filledTextColor: Int
    private val filledBackgroundImage: Drawable?
    private val filledFont: Typeface?

    // endregion

    private var onFinishFunction: ((String) -> Unit) = {}
    private var onCharacterUpdatedFunction: ((Boolean) -> Unit) = {}

    private val editTexts: MutableList<EditText> = mutableListOf()
    private var focusIndex = 0

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.OTPView,
            0, 0
        ).apply {
            try {
                itemCount = getInteger(R.styleable.OTPView_otp_itemCount, 1)
                showCursor = getBoolean(R.styleable.OTPView_otp_showCursor, false)
                underscoreCursor = getBoolean(R.styleable.OTPView_otp_underscoreCursor, false)
                customCursorDrawable = getDrawable(R.styleable.OTPView_otp_customCursor)
                inputType = getInteger(R.styleable.OTPView_android_inputType, 0)
                importantForAutofillLocal =
                    getInteger(R.styleable.OTPView_android_importantForAutofill, 0)
                autofillHints = getString(R.styleable.OTPView_android_autofillHints)
                itemWidth = getDimensionPixelSize(R.styleable.OTPView_otp_itemWidth, 44)
                itemHeight = getDimensionPixelSize(R.styleable.OTPView_otp_itemHeight, 44)
                cursorColor = getColor(R.styleable.OTPView_otp_cursorColor, Color.BLACK)
                allCaps = getBoolean(R.styleable.OTPView_otp_allcaps, false)
                marginBetween = getDimensionPixelSize(
                    R.styleable.OTPView_otp_marginBetween,
                    8.dpTopX
                )
                isPassword = getBoolean(R.styleable.OTPView_otp_ispassword, false)

                textSizeDefault =
                    getDimensionPixelSize(R.styleable.OTPView_otp_textSize, 14.dpTopX)
                textColor = getInteger(R.styleable.OTPView_otp_textColor, Color.BLACK)
                backgroundImage =
                    getDrawable(R.styleable.OTPView_otp_backgroundImage) ?: customBackground()
                font =
                    ResourcesCompat.getFont(
                        context,
                        getResourceId(R.styleable.OTPView_otp_Font, R.font.splinesans_regular)
                    )

                highlightedTextSize = getDimensionPixelSize(
                    R.styleable.OTPView_otp_highlightedTextSize,
                    textSizeDefault
                )
                highlightedTextColor = getInteger(
                    R.styleable.OTPView_otp_highlightedTextColor,
                    textColor
                )
                highlightedBackgroundImage =
                    getDrawable(R.styleable.OTPView_otp_highlightedBackgroundImage)
                        ?: backgroundImage
                highlightedFont = ResourcesCompat.getFont(
                    context,
                    getResourceId(R.styleable.OTPView_otp_highlightedFont, R.font.splinesans_regular)
                )

                filledTextSize = getDimensionPixelSize(
                    R.styleable.OTPView_otp_filledTextSize,
                    textSizeDefault
                )
                filledTextColor = getInteger(R.styleable.OTPView_otp_filledTextColor, textColor)
                filledBackgroundImage =
                    getDrawable(R.styleable.OTPView_otp_filledBackgroundImage)
                        ?: backgroundImage
                filledFont = ResourcesCompat.getFont(
                    context,
                    getResourceId(R.styleable.OTPView_otp_filledFont, R.font.splinesans_regular)
                ) ?: font

                initEditTexts()
            } finally {
                recycle()
            }
        }
    }

    private var disableEditListener: Boolean = false

    // region Init

    private fun initEditTexts() {
        for (x in 0 until itemCount) {
            addEditText(x)
            addListenerForIndex(x)
        }

        styleEditTexts()
        val et = editTexts[0]
        et.postDelayed({
            val editText = editTexts[focusIndex]
            editText.requestFocus()
            styleEditTexts()
            showKeyboard(true, editText)
        }, 100)
    }

    private fun addListenerForIndex(index: Int) {
        editTexts[index].addTextChangedListener(object : TextWatcher {

            var beforeText: String = ""
            var afterText: String = ""

            val isCopy: Boolean
                get() = (afterText.count() - beforeText.count()) > 1

            override fun afterTextChanged(s: Editable?) {
                if (!disableEditListener) {
                    when {
                        editTexts[index].text.isEmpty() -> {
                            changeFocus(false)
                        }

                        editTexts[index].text.length > 1 -> {

                            // Only Taking the last char
                            s?.let {
                                if (isCopy) {
                                    setText(it.toString(), index, false)
                                } else {
                                    editTexts[index].setText(it.first().toString())
                                }
                            }
                        }

                        else -> {
                            changeFocus(true)
                        }
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                print("Before Text Changed! ${s.toString()} $start $count $after")
                beforeText = s.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                print("on Text Changed! ${s.toString()} $start $count $before")
                afterText = s.toString()
            }
        })
        editTexts[index].setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL &&
                event.action == KeyEvent.ACTION_DOWN
            ) {
                disableEditListener = true
                editTexts[index].setText("")
                changeFocus(false)
                //if(index-1 >= 0)
                //editTexts[index - 1].setText("")
                disableEditListener = false
            }
            if (event.action == KeyEvent.ACTION_DOWN &&
                keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                if (isFilled())
                    onFinishFunction(getStringFromFields())
            }
            return@setOnKeyListener false
        }
        editTexts[index].setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                focusIndex = index
            styleEditTexts()
            v.post {
                if (focusIndex < editTexts.size)
                    editTexts[focusIndex].setSelection(0)
            }
        }

        if (isPassword) {
            editTexts.forEach {
                it.transformationMethod =
                    AsteriskPasswordTransformationMethod()
            }
        }
    }

    private fun changeFocus(increment: Boolean) {
        if (increment) focusIndex++ else focusIndex--

        when {
            focusIndex < 0 -> focusIndex = 0
            focusIndex < editTexts.size -> {
                editTexts[focusIndex].requestFocus()
            }

            else -> {
                editTexts.forEach {
                    it.clearFocus()
                }
                showKeyboard(false, editTexts.last())
                if (isFilled())
                    onFinishFunction(getStringFromFields())
            }
        }
        onCharacterUpdatedFunction(isFilled())
        styleEditTexts()
    }

    private fun addEditText(index: Int) {
        val et = EditText(context)

        // All

        et.isCursorVisible = showCursor

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            customCursorDrawable?.let {
                et.textCursorDrawable = it
            } ?: kotlin.run {
                if (underscoreCursor) {
                    et.textCursorDrawable =
                        ResourcesCompat.getDrawable(resources, R.drawable.otp_underscore, context.theme)
                }
            }
        }
        et.inputType = inputType
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            et.importantForAutofill = importantForAutofillLocal
            et.setAutofillHints(autofillHints)
        }
        val params = LayoutParams(
            itemWidth,
            itemHeight
        )

        et.isAllCaps = allCaps

        val leftDp = if (index == 0) 8.dpTopX else 0.dpTopX

        params.setMargins(
            leftDp,
            8.dpTopX,
            marginBetween,
            8.dpTopX
        )
        et.layoutParams = params
        et.gravity = Gravity.CENTER
        //et.filters = arrayOf<InputFilter>(LengthFilter(1))

        // Default
        styleDefault(et)

        et.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                et.post { et.setSelection(0) }
            }
        }

        editTexts.add(et)
        otpViewLayoutBinding.otpWrapper.addView(et)
    }

    // endregion

    // region Styling

    private fun styleEditTexts() {
        for (x in 0 until editTexts.size) {
            val et = editTexts[x]
            if (x < focusIndex) {
                styleFilled(et)
                return
            }
            if (x == focusIndex) {
                styleHighlighted(et)
                return
            }
            styleDefault(et)
        }
    }

    private fun styleDefault(editText: EditText) {
        editText.apply {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeDefault.toFloat())
            setTextColor(textColor)
            background = backgroundImage
            typeface = font
        }
    }

    private fun styleHighlighted(editText: EditText) {
        editText.apply {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, highlightedTextSize.toFloat())
            setTextColor(highlightedTextColor)
            background = highlightedBackgroundImage
            typeface = highlightedFont
        }
    }

    private fun styleFilled(editText: EditText) {
        editText.apply {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, filledTextSize.toFloat())
            setTextColor(filledTextColor)
            background = filledBackgroundImage
            typeface = filledFont
        }
    }

    // endregion

    // region Utility

    private val Int.dpTopX: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()

    private fun customBackground(): Drawable {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadius = 8.dpTopX.toFloat()
        shape.setColor(Color.WHITE)
        shape.setStroke(2.dpTopX, Color.BLACK)
        return shape
    }

    private fun showKeyboard(show: Boolean, editText: EditText) {

        val imm: InputMethodManager? =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        if (show) {
            imm?.showSoftInput(editText, 0)
        } else {
            imm?.hideSoftInputFromWindow(editText.applicationWindowToken, 0)
        }
    }

    private fun setText(str: String, index: Int, shouldClearRest: Boolean) {
        var customString = str.take(itemCount - index)
        disableEditListener = true
        for (editTextIndex in index until editTexts.size) {
            if (customString.isNotEmpty()) {
                editTexts[editTextIndex].setText(
                    if (allCaps) customString.first().toString().uppercase(Locale.ROOT)
                    else customString.first().toString()
                )
                customString = customString.removeRange(0, 1)
            } else if (shouldClearRest) {
                editTexts[editTextIndex].setText("")
            }
        }
        if (customString.count() < editTexts.size) {
            focusIndex = customString.count()
            disableEditListener = false
            showKeyboard(true, editTexts[focusIndex])
        } else {
            editTexts.forEach {
                it.clearFocus()
            }
            focusIndex = editTexts.size
            disableEditListener = false
            showKeyboard(false, editTexts.last())
        }
        styleEditTexts()
    }

    // endregion

    // region Public

    fun isFilled(): Boolean {
        editTexts.forEach {
            if (it.text.isNullOrBlank()) return false
        }
        return true
    }

    fun getStringFromFields(): String {
        var str = ""
        editTexts.forEach {
            str += it.text.firstOrNull()
        }

        return if (allCaps) str.uppercase(Locale.ROOT) else str
    }

    fun setOnFinishListener(func: (String) -> Unit) {
        onFinishFunction = func
    }

    fun setOnCharacterUpdatedListener(func: (Boolean) -> Unit) {
        onCharacterUpdatedFunction = func
    }

    fun setText(str: String) {
        setText(str, 0, true)
    }

    fun clearText(showKeyboard: Boolean) {
        disableEditListener = true
        for (x in 0 until editTexts.size) {
            editTexts[x].setText("")
        }
        focusIndex = 0
        disableEditListener = false
        showKeyboard(showKeyboard, editTexts[focusIndex])
    }

    fun fitToWidth(width: Int) {
        val outerMargin = 8.dpTopX
        var dividedSpace = (width - (outerMargin * 2)) / editTexts.size
        dividedSpace -= marginBetween
        itemWidth = dividedSpace
        itemHeight = (itemWidth * 1.25f).toInt()

        val params = LayoutParams(
            itemWidth,
            itemHeight
        )

        editTexts.forEachIndexed { index, editText ->
            val leftDp = if (index == 0) 8.dpTopX else 0.dpTopX

            params.setMargins(
                leftDp,
                8.dpTopX,
                marginBetween,
                8.dpTopX
            )
            editText.layoutParams = params
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        editTexts.forEach {
            it.isEnabled = enabled
        }
    }

    fun copyText() {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData = ClipData.newPlainText("Copied", getStringFromFields())
        clipboard.setPrimaryClip(clip)
    }

    fun pasteText() {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.primaryClip?.let {
            val item = it.getItemAt(0)
            val pasteData: String = item.text.toString()
            setText(pasteData)
        }
    }

    // endregion
}

private class AsteriskPasswordTransformationMethod : PasswordTransformationMethod() {
    override fun getTransformation(source: CharSequence?, view: View?): CharSequence {
        return PasswordCharSequence(source!!)
    }
}

private class PasswordCharSequence(private var mSource: CharSequence) : CharSequence {

    override val length: Int
        get() = mSource.length

    override fun get(index: Int): Char = '*'

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence =
        mSource.subSequence(startIndex, endIndex)
}