package com.xvantage.rental.utils


import android.animation.ObjectAnimator
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.StyleSpan
import android.util.Base64
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.Transformation
import android.view.inputmethod.InputMethodManager
import android.webkit.URLUtil
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonDeserializer
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.xvantage.rental.R
import com.xvantage.rental.utils.constants.ApiConstant
import com.xvantage.rental.utils.constants.Constant
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.lang.reflect.Type
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLDecoder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.Locale
import java.util.regex.Pattern

object Utility {
    const val TAG: String = "Utility"

    /**
     * change datepicker view
     *
     * @param datePicker
     */
    fun changeDatePickerView(mContext: Context, datePicker: DatePicker) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android")
            if (daySpinnerId != 0) {
                val daySpinner = datePicker.findViewById<View>(daySpinnerId)
                if (daySpinner != null) {
                    changeDatePickerColor(mContext, daySpinner)
                    /*if (isDay) {
                        daySpinner.setVisibility(View.VISIBLE);
                    } else {
                        daySpinner.setVisibility(View.GONE);
                    }*/
                }
            }

            val monthSpinnerId = Resources.getSystem().getIdentifier("month", "id", "android")
            if (monthSpinnerId != 0) {
                val monthSpinner = datePicker.findViewById<View>(monthSpinnerId)
                if (monthSpinner != null) {
                    changeDatePickerColor(mContext, monthSpinner)
                    /*if (isMonth) {
                        monthSpinner.setVisibility(View.VISIBLE);
                    } else {
                        monthSpinner.setVisibility(View.GONE);
                    }*/
                }
            }

            val yearSpinnerId = Resources.getSystem().getIdentifier("year", "id", "android")
            if (yearSpinnerId != 0) {
                val yearSpinner = datePicker.findViewById<View>(yearSpinnerId)
                if (yearSpinner != null) {
                    changeDatePickerColor(mContext, yearSpinner)
                    /*if (isYear) {
                        yearSpinner.setVisibility(View.VISIBLE);
                    } else {
                        yearSpinner.setVisibility(View.GONE);
                    }*/
                }
            }
        } else {
            val f = datePicker.javaClass.declaredFields
            for (field in f) {
                if (field.name == "mDayPicker" || field.name == "mDaySpinner") {
                    field.isAccessible = true
                    var dayPicker: Any? = null
                    try {
                        dayPicker = field[datePicker]
                    } catch (e: IllegalAccessException) {
                        e.printStackTrace()
                    }
                    changeDatePickerColor(mContext, (dayPicker as View?))
                    /*if (isDay) {
                        ((View) dayPicker).setVisibility(View.VISIBLE);
                    } else {
                        ((View) dayPicker).setVisibility(View.GONE);
                    }*/
                }

                if (field.name == "mMonthPicker" || field.name == "mMonthSpinner") {
                    field.isAccessible = true
                    var monthPicker: Any? = null
                    try {
                        monthPicker = field[datePicker]
                    } catch (e: IllegalAccessException) {
                        e.printStackTrace()
                    }
                    changeDatePickerColor(mContext, (monthPicker as View?))
                    /*if (isMonth) {
                        ((View) monthPicker).setVisibility(View.VISIBLE);
                    } else {
                        ((View) monthPicker).setVisibility(View.GONE);
                    }*/
                }

                if (field.name == "mYearPicker" || field.name == "mYearSpinner") {
                    field.isAccessible = true
                    var yearPicker: Any? = null
                    try {
                        yearPicker = field[datePicker]
                    } catch (e: IllegalAccessException) {
                        e.printStackTrace()
                    }
                    changeDatePickerColor(mContext, (yearPicker as View?))
                    /*if (isYear) {
                        ((View) yearPicker).setVisibility(View.VISIBLE);
                    } else {
                        ((View) yearPicker).setVisibility(View.GONE);
                    }*/
                }
            }
        }
    }

    /**
     * change datepicker color
     *
     * @param mContext
     * @param view
     */
    fun changeDatePickerColor(mContext: Context, view: View?) {
        try {
            val dividerField = view!!.javaClass.getDeclaredField("mSelectionDivider")
            dividerField.isAccessible = true

            val colorDrawable = ColorDrawable(mContext.resources.getColor(R.color.black_cole))
            dividerField[view] = colorDrawable
            view.invalidate()
        } catch (e: NoSuchFieldException) {
            Debugger.logE("setNumberPickerTxtClr", e.message!!)
        } catch (e: IllegalAccessException) {
            Debugger.logE("setNumberPickerTxtClr", e.message!!)
        } catch (e: IllegalArgumentException) {
            Debugger.logE("setNumberPickerTxtClr", e.message!!)
        }
    }

    /**
     * get transition name
     *
     * @param view
     * @return
     */
    fun getTransitionName(view: View?): String? {
        return ViewCompat.getTransitionName(view!!)
    }

    /**
     * This method checks if the Network available on the device or not.
     *
     * @param mContext
     * @return true if network available, false otherwise
     */
    fun isNetworkAvailable(mContext: Context): Boolean {
        var connected = false
        try {
            val cm = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            if (netInfo != null && netInfo.isConnectedOrConnecting) {
                connected = true
            } else if (netInfo != null && netInfo.isConnected &&
                cm.activeNetworkInfo!!.isAvailable
            ) {
                connected = true
            } else if (netInfo != null && netInfo.isConnected) {
                try {
                    val url = URL("http://www.google.com")
                    val urlc = url.openConnection() as HttpURLConnection
                    urlc.connectTimeout = 3000
                    urlc.connect()
                    if (urlc.responseCode == ApiConstant.STATUS_CODE_SUCCESS) {
                        connected = true
                    }
                } catch (e: MalformedURLException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else if (cm != null) {
                val netInfoAll = cm.allNetworkInfo
                for (ni in netInfoAll) {
                    if ((ni.typeName.equals("WIFI", ignoreCase = true) || ni
                            .typeName.equals("MOBILE", ignoreCase = true))
                        && ni.isConnected && ni.isAvailable
                    ) {
                        connected = true
                        if (connected) {
                            break
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return connected
    }

    val deviceLanguage: String
        /**
         * get locale - device's default language
         *
         * @return
         */
        get() {
            try {
                return Locale.getDefault().toString()
            } catch (e: Exception) {
                e.printStackTrace()
                return ""
            }
        }

    /*val appVersion: String
        *//**
         * get application version
         *
         * @return
         *//*
        get() {
            try {
                return BuildConfig.VERSION_NAME
            } catch (e: Exception) {
                e.printStackTrace()
                return ""
            }
        }*/

    /**
     * get application version
     *
     * @param mContext
     * @return
     */
    fun getPackageName(mContext: Context): String {
        try {
            return mContext.packageManager.getPackageInfo(mContext.packageName, 0).packageName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return ""
        }
    }

    /**
     * find your keyhash for facebook from package name
     *
     * @param mContext
     */
    fun getFacebookKeyHash(mContext: Context) {
        try {
            val info = mContext.packageManager.getPackageInfo(
                getPackageName(mContext),
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures!!) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
    }

    /**
     * show keyboard
     *
     * @param mContext
     * @param view
     */
    fun showSoftKeyboard(mContext: Context, view: View?) {
        try {
            val imm = mContext.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * hide keyboard if visible
     *
     * @param mActivity
     * @param view
     */
    fun hideSoftKeyboard(mActivity: Activity, view: View) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
                hideSoftKeyboard(mActivity)
                false
            }
        }

        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                hideSoftKeyboard(mActivity, innerView)
            }
        }
    }

    /**
     * hide keyboard if visible
     *
     * @param mActivity
     */
    fun hideSoftKeyboard(mActivity: Activity) {
        try {
            val imm =
                mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            // Find the currently focused view, so we can grab the correct window token from it.
            var view = mActivity.currentFocus
            // If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(mActivity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * visible password hint
     *
     * @param view
     */
    fun visiblePasswordHint(view: View) {
        if (view.visibility == View.GONE) {
            view.visibility = View.VISIBLE
            view.alpha = 0f
            view.post {
                ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).setDuration(300).start()
                ObjectAnimator.ofFloat(
                    view,
                    "translationY",
                    -(view.height / 4).toFloat(),
                    0f
                ).setDuration(300).start()
            }
        }
    }

    /**
     * focus on view
     *
     * @param editText
     */
    fun requestSelection(editText: EditText) {
        if (!TextUtils.isEmpty(editText.text.toString().trim { it <= ' ' })) {
            editText.setSelection(editText.text.toString().trim { it <= ' ' }.length)
        }
    }

    /**
     * focus on view
     *
     * @param mActivity
     * @param view
     */
    fun requestFocus(mActivity: Activity, view: View) {
        if (view.requestFocus()) {
            mActivity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    /**
     * multiline EditText make scrollable
     *
     * @param editText
     */
    fun enableEditTextScolling(editText: EditText) {
        editText.setOnTouchListener { view, motionEvent ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            when (motionEvent.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_UP -> view.parent.requestDisallowInterceptTouchEvent(false)
            }
            false
        }
    }

    /**
     * get sub string from source string
     *
     * @param sourceString
     * @param length
     * @return
     */
    fun getlastSubString(sourceString: String, length: Int): String {
        try {
            val subStringLength = sourceString.length
            if (subStringLength <= length) {
                return sourceString
            }
            val startIndex = subStringLength - length
            return sourceString.substring(startIndex)
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    /**
     * decode source string into UTF-8
     *
     * @param sourceString
     * @return
     */
    fun decodeString(sourceString: String): String {
        try {
            return URLDecoder.decode(sourceString, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            return sourceString
        }
    }

    /**
     * encrypt source string to base64
     *
     * @param sourceString
     * @return
     */
    fun encodeString(sourceString: String): String {
        return Base64.encodeToString(sourceString.toByteArray(), Base64.NO_WRAP or Base64.URL_SAFE)
    }

    /**
     * check given url is http/https
     *
     * @param url
     * @return
     */
    fun isHttpsUrl(url: String?): Boolean {
        return if (URLUtil.isHttpsUrl(url)) {
            true
        } else {
            false
        }
    }

    /**
     * convert source string to camelcase
     *
     * @param sourceString
     * @return
     */
    fun convertStringToCamelLetter(sourceString: String): String {
        return sourceString.substring(0, 1).uppercase(Locale.getDefault()) + sourceString.substring(
            1
        ).lowercase(
            Locale.getDefault()
        )
    }

    /**
     * source string make bold to given spanned text
     *
     * @param mContext
     * @param sourceString
     * @param spannedText
     * @return
     */
    fun spannableString(
        mContext: Context?,
        sourceString: String?,
        spannedText: String?
    ): Spannable {
        if (sourceString == null && sourceString!!.trim { it <= ' ' }.length == 0) {
            return SpannableString("")
        }

        val ss: Spannable = SpannableString(sourceString.trim { it <= ' ' })
        val pattern = Pattern.compile(Pattern.quote(spannedText))
        val matcher = pattern.matcher(ss)
        while (matcher.find()) {
            ss.setSpan(
                StyleSpan(Typeface.BOLD),
                matcher.start(), matcher.end(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        return ss
    }

    /**
     * remove '_' from source string and convert to camelcase
     *
     * @param sourceString
     * @return
     */
    fun removeUnderscoreAndConvertToCamelLetter(sourceString: String): String {
        try {
            val parts = sourceString.split("_".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            var formattedValue = ""
            for (part in parts) {
                formattedValue = formattedValue + " " + convertStringToCamelLetter(part)
            }

            return formattedValue
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    /**
     * load JSON from assets .json file
     *
     * @param mContext
     * @param sourceFile
     * @return
     */
    fun loadJSONFromAsset(mContext: Context, sourceFile: String): String? {
        var json: String? = null
        try {
            val `is` = mContext.assets.open("$sourceFile.json")
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer, charset("UTF-8"))
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return json
    }

    /**
     * json to pojo with deserializer
     *
     * @param jsonString
     * @param pojoClass
     * @param jsonDeserializer
     * @param pojoType
     * @return
     */
    fun jsonToPojoWithDeserializer(
        jsonString: String?,
        pojoClass: Class<*>?,
        jsonDeserializer: JsonDeserializer<*>?,
        pojoType: Type?
    ): Any {
        return GsonBuilder().registerTypeAdapter(pojoType, jsonDeserializer).create()
            .fromJson(jsonString, pojoClass)
    }

    /**
     * json to pojo with type class
     *
     * @param jsonString
     * @param pojoType
     * @return
     */
    fun jsonToPojo(jsonString: String?, pojoType: Type?): Any {
        return Gson().fromJson(jsonString, pojoType)
    }

    /**
     * json to pojo
     *
     * @param jsonString
     * @param pojoClass
     * @return
     */
    fun jsonToPojo(jsonString: String?, pojoClass: Class<*>?): Any {
        return Gson().fromJson(jsonString, pojoClass)
    }

    /**
     * list convert to JsonArray
     *
     * @param object
     * @return
     */
    fun objectToJsonArray(`object`: Any?): JsonArray {
        val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

        val jsonParser = JsonParser()
        val jsonElement = jsonParser.parse(gson.toJson(`object`))
        return jsonElement.asJsonArray
    }

    /**
     * list convert to JsonArray
     *
     * @param object
     * @return
     */
    fun objectToJsonObject(`object`: Any?): JsonObject {
        val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

        val jsonParser = JsonParser()
        val jsonElement = jsonParser.parse(gson.toJson(`object`))
        return jsonElement.asJsonObject
    }

    /**
     * string convert to JsonObject
     *
     * @param json
     * @return
     */
    fun stringToJsonObject(json: String?): JsonObject {
        val jsonParser = JsonParser()
        val jsonElement = jsonParser.parse(Gson().toJson(json))
        return jsonElement.asJsonObject
    }

    /**
     * pojo to json
     *
     * @param pojoObject
     * @return
     */
    fun pojoToJson(pojoObject: Any?): String? {
        return if (pojoObject != null) {
            Gson().toJson(pojoObject)
        } else {
            null
        }
    }

    /**
     * string convert to camelcase
     *
     * @param value
     * @return
     */
    fun toCamelCase(value: String?): String {
        if (value == null) {
            return ""
        }

        var space = true
        val builder = StringBuilder(value)
        val length = builder.length
        for (i in 0 until length) {
            val c = builder[i]
            if (space) {
                if (!Character.isWhitespace(c)) {
                    builder.setCharAt(i, c.titlecaseChar())
                    space = false
                }
            } else if (Character.isWhitespace(c)) {
                space = true
            } else {
                builder.setCharAt(i, c.lowercaseChar())
            }
        }

        return builder.toString()
    }

    /**
     * expand view
     *
     * @param view
     */
    fun expandView(view: View) {
        view.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val targetHeight = view.measuredHeight

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        view.layoutParams.height = 1
        view.visibility = View.VISIBLE
        val animation: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                view.layoutParams.height =
                    if (interpolatedTime == 1f) LinearLayout.LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
                view.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        animation.duration =
            (targetHeight / view.context.resources.displayMetrics.density).toInt().toLong()
        view.startAnimation(animation)
    }

    /**
     * collapse view
     *
     * @param view
     */
    fun collapseView(view: View) {
        val initialHeight = view.measuredHeight

        val animation: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    view.visibility = View.GONE
                } else {
                    view.layoutParams.height =
                        initialHeight - (initialHeight * interpolatedTime).toInt()
                    view.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        animation.duration =
            (initialHeight / view.context.resources.displayMetrics.density).toInt().toLong()
        view.startAnimation(animation)
    }

    val isLoggedIn: Boolean
        get() = true
           /* get() = if (PreferenceUtils.getInstance().isLoggedIn() && PreferenceUtils.getInstance()
                .getUser() != null
        ) {
            true
        } else {
            false
        }*/
    /**
     * double value conversion
     *
     * @param value
     * @return
     */
    fun doubleValueConversionForPrice(value: Float): String {
        try {
            return if (value == 0f) {
                ""
            } else {
                Constant.const_doller + String.format("%.2f", value)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    /**
     * double value conversion
     */
    fun doubleValueConversionForTotal(value: Float): String {
        try {
            return Constant.const_doller + String.format("%.2f", value)
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    /**
     * get value with comma seperated
     *
     * @param list
     * @return
     */
    fun getValue(list: ArrayList<String?>): String {
        val sb_value = StringBuilder()
        try {
            for (value in list) {
                sb_value.append(value).append(",")
            }

            if (sb_value.length > 1) {
                sb_value.deleteCharAt(sb_value.lastIndexOf(","))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return sb_value.toString()
    }

    fun getImageUri(mActivity: Activity, bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(mActivity.contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }

    fun getPath(mActivity: Activity, uri: Uri): String? {
        try {
            val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
            if (isKitKat && DocumentsContract.isDocumentUri(mActivity, uri)) {
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    val type = split[0]

                    if ("primary".equals(type, ignoreCase = true)) {
                        return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                    }
                } else if (isDownloadsDocument(uri)) {
                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        id.toLong()
                    )

                    return getDataColumn(mActivity, contentUri, null, null)
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    val type = split[0]
                    var contentUri: Uri? = null
                    if ("image" == type) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])
                    return getDataColumn(mActivity, contentUri, selection, selectionArgs)
                }
            } else if ("content".equals(uri.scheme, ignoreCase = true)) {
                if (isGooglePhotosUri(uri)) return uri.lastPathSegment
                return getDataColumn(mActivity, uri, null, null)
            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                return uri.path
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    fun getDataColumn(
        mActivity: Activity,
        uri: Uri?,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor =
                mActivity.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }

        return null
    }

    fun getBitmapFromPath(path: String?): Bitmap? {
        try {
            var bitmap: Bitmap? = null
            val f = File(path)
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888

            bitmap = BitmapFactory.decodeStream(FileInputStream(f), null, options)
            return bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}