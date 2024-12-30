package com.xvantage.rental.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CommonFunction {

    var invoiceDatePicker: Calendar = Calendar.getInstance()

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

    fun showDatePickerDialog(
        context: Context,
        initialDate: Calendar = Calendar.getInstance(),
        dateFormat: String = "dd MMM, yyyy",
        onDateSelected: (String) -> Unit
    ) {
        val dateListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            // Update the calendar with the selected date
            initialDate[Calendar.YEAR] = year
            initialDate[Calendar.MONTH] = monthOfYear
            initialDate[Calendar.DAY_OF_MONTH] = dayOfMonth

            // Format the date and return it through the callback
            val sdf = SimpleDateFormat(dateFormat, Locale.US)
            onDateSelected(sdf.format(initialDate.time))
        }

        // Create and show the DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            context,
            AlertDialog.THEME_HOLO_LIGHT, // Change theme as needed
            dateListener,
            initialDate.get(Calendar.YEAR),
            initialDate.get(Calendar.MONTH),
            initialDate.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }



}