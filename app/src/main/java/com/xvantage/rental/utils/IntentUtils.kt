package com.xvantage.rental.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import java.util.*

class IntentUtils private constructor() {

    companion object {
        const val RESULTCODE_CAMERA = 5001
        const val RESULTCODE_GALLERY = 5002
        const val RESULTCODE_ACCOUNT = 5003
        const val RESULTCODE_BOOKING = 5003
        const val CROP_IMAGE_ACTIVITY_REQUEST_CODE = 203

        @Volatile
        private var instance: IntentUtils? = null

        fun getInstance(): IntentUtils {
            return instance ?: synchronized(this) {
                instance ?: IntentUtils().also { instance = it }
            }
        }
    }

    /**
     * Check if activity is running
     */
    @RequiresApi(Build.VERSION_CODES.M)
    fun isActivityVisible(context: Context, activity: String): Boolean {
        var isActivityVisible = false
        try {
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                val runningProcesses = am.appTasks
                for (processInfo in runningProcesses) {
                    if (processInfo.taskInfo.topActivity?.className.equals(activity, ignoreCase = true)) {
                        isActivityVisible = true
                    }
                }
            } else {
                val taskInfo = am.getRunningTasks(1)
                val componentInfo = taskInfo[0].topActivity
                if (componentInfo!!.className.equals(activity, ignoreCase = true)) {
                    isActivityVisible = true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        println("$activity visible:: $isActivityVisible")
        return isActivityVisible
    }

    /**
     * Open camera
     */
    fun openCamera(context: Context) {
        try {
            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Open browser
     */
    fun openBrowser(context: Context, link: String?) {
        try {
            if (link.isNullOrEmpty()) return
            var modifiedLink = link
            if (!modifiedLink.startsWith("http://") && !modifiedLink.startsWith("https://")) {
                modifiedLink = "http://$modifiedLink"
            }
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(modifiedLink)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Send email
     */
    fun sendEmail(context: Context, email: String?, subject: String?, body: String?) {
        try {
            val emailIntent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email ?: ""))
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, body)
                type = "text/plain"
            }
            val pm = context.packageManager
            val matches = pm.queryIntentActivities(emailIntent, 0)
            val best = matches.find {
                it.activityInfo.packageName.endsWith(".gm") || it.activityInfo.name.contains("gmail", ignoreCase = true)
            }
            best?.let {
                emailIntent.setClassName(it.activityInfo.packageName, it.activityInfo.name)
                context.startActivity(emailIntent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

   /* *//**
     * Open map and draw path
     *//*
    fun openMap(context: Context, sourceLat: Double, sourceLong: Double, destinationLat: String, destinationLong: String, name: String) {
        try {
            val uri = String.format(Locale.ENGLISH, "geo:%f,%f?z=17&q=loc:%f,%f,%s",
                sourceLat, sourceLong, destinationLat.toDouble(), destinationLong.toDouble(), "($name)")
            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri)).apply {
                setPackage("com.google.android.apps.maps")
            }
            context.startActivity(mapIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, context.getString(R.string.msg_install_google_maps), Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
*/
    /**
     * Make phone call
     */
    fun makePhoneCall(context: Context, phone: String?) {
        try {
            if (phone.isNullOrEmpty()) return
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse("tel:$phone")
            }
            context.startActivity(intent)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    /**
     * Screen transition when activity opens
     *//*
    fun transitionNext(activity: Activity) {
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    *//**
     * Screen transition when activity closes
     *//*
    fun transitionPrevious(activity: Activity) {
        activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }*/

    /**
     * Navigate to next activity
     */
    fun navigateToNextActivity(
        activity: Activity,
        fragment: Fragment?,
        classActivity: Class<*>,
        bundle: Bundle,
        options: Bundle
    ) {
        val intent = Intent(activity, classActivity).apply {
            putExtras(bundle)
        }
        fragment?.startActivity(intent, options) ?: activity.startActivity(intent, options)
//        transitionNext(activity)
    }

    /**
     * Navigate to next activity with result
     */
    fun navigateToNextActivity(
        activity: Activity,
        fragment: Fragment?,
        classActivity: Class<*>,
        bundle: Bundle,
        options: Bundle,
        resultCode: Int
    ) {
        val intent = Intent(activity, classActivity).apply {
            putExtras(bundle)
        }
        fragment?.startActivityForResult(intent, resultCode, options)
            ?: activity.startActivityForResult(intent, resultCode, options)
//        transitionNext(activity)
    }

    /**
     * Navigate to next activity and clear stack
     */
    fun navigateToNextActivity(
        activity: Activity,
        classActivity: Class<*>,
        bundle: Bundle,
        clearStack: Boolean
    ) {
        val intent = Intent(activity, classActivity).apply {
            putExtras(bundle)
            if (clearStack) {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        }
        activity.startActivity(intent)
//        transitionNext(activity)
    }
}
