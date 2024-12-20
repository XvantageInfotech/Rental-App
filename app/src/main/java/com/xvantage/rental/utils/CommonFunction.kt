package com.xvantage.rental.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.xvantage.rental.ui.addProperty.AddPropertyActivity
import com.xvantage.rental.ui.registration.SignUpActivity

class CommonFunction {

    fun navigation(context: Context, activity: Class<out Activity>) {
        val intent = Intent(context, activity)
        context.startActivity(intent)
    }
    fun toast(context: Context, message:String) {
        Toast.makeText(context, "$message", Toast.LENGTH_SHORT).show()
    }
}