package com.xvantage.rental.utils

import android.content.Context
import android.content.SharedPreferences


class AppPreference(context: Context) {

    private var appSharedPrefs: SharedPreferences =
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    private val editor: SharedPreferences.Editor = appSharedPrefs.edit()


    fun isUserLoginFirstTime(): Boolean {
        return appSharedPrefs.getBoolean("isUserLoginFirstTime", false)
    }

    fun setUserLoginFirstTime(value: Boolean?) {
        editor.putBoolean("isUserLoginFirstTime", value!!)
        editor.commit()
    }

    fun isFirstTimePreview(): Boolean {
        return appSharedPrefs.getBoolean("isFirstTimePreview", false)
    }

    fun setFirstTimePreview(value: Boolean?) {
        editor.putBoolean("isFirstTimePreview", value!!)
        editor.commit()
    }

    fun isRemove(): Boolean {
        return appSharedPrefs.getBoolean("isRemove", false)
    }

    fun setRemove(value: Boolean?) {
        editor.putBoolean("isRemove", value!!)
        editor.commit()
    }

    /*......*/
    fun isFirst(): Boolean {
        return appSharedPrefs.getBoolean("isFirst", false)
    }

    fun setFirst(value: Boolean?) {
        editor.putBoolean("isFirst", value!!)
        editor.commit()
    }

    fun isUserLogin(): Boolean {
        return appSharedPrefs.getBoolean("isUserLogin", false)
    }

    fun setUId(data: String?) {
        editor.putString("UId", data)
        editor.commit()
    }

    fun getUId(): String? {
        return appSharedPrefs.getString("UId", "")
    }

    fun clearPreferences() {
        editor.clear()
        editor.apply()
    }
}