package com.xvantage.rental.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.xvantage.rental.ui.addProperty.AddPropertyActivity
import com.xvantage.rental.ui.registration.SignUpActivity
import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import android.text.format.Formatter

class CommonFunction {

    fun navigation(context: Context, activity: Class<out Activity>) {
        val intent = Intent(context, activity)
        context.startActivity(intent)
    }
    fun toast(context: Context, message:String) {
        Toast.makeText(context, "$message", Toast.LENGTH_SHORT).show()
    }

    fun getFileName(context: Context, uri: Uri): String {
        var fileName = "Unknown"
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                fileName = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
            }
        }
        return fileName
    }

    fun getFileSize(context: Context, uri: Uri): String {
        var fileSize = "Unknown"
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
                if (sizeIndex != -1) {
                    val size = it.getLong(sizeIndex)
                    fileSize = Formatter.formatFileSize(context, size)
                }
            }
        }
        return fileSize
    }


}