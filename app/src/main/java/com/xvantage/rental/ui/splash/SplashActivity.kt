package com.xvantage.rental.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import com.xvantage.rental.R
import com.xvantage.rental.databinding.ActivitySplashBinding
import com.xvantage.rental.ui.addTenant.AddTenantActivity
import com.xvantage.rental.ui.dashboard.DashboardActivity
import com.xvantage.rental.ui.onboarding.BoardingScreenActivity
import com.xvantage.rental.ui.registration.SignUpActivity
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
            if (appPreference.isUserLoginFirstTime()) {
                val intent = Intent(this, DashboardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } else if (!appPreference.isFirstTimePreview()) {
                val intent = Intent(this, BoardingScreenActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } else{
                val intent = Intent(this, SignUpActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }, 2000)

    }
}