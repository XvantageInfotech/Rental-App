package com.xvantage.rental.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.xvantage.rental.MainActivity
import com.xvantage.rental.R
import com.xvantage.rental.databinding.ActivitySplashBinding
import com.xvantage.rental.ui.onboarding.BoardingScreenActivity
import com.xvantage.rental.utils.AppPreference

class SplashActivity : AppCompatActivity() {

    private lateinit var layoutBinding: ActivitySplashBinding
    lateinit var appPreference: AppPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        layoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        appPreference = AppPreference(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        Handler(Looper.getMainLooper()).postDelayed({
            if (!appPreference.isUserLoginFirstTime()) {
                appPreference.setUserLoginFirstTime(true)
                val intent = Intent(this, BoardingScreenActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } else {
                appPreference.setUserLoginFirstTime(true)
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }, 2000)

    }
}