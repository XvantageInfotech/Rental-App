package com.xvantage.rental.ui.onboarding

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.xvantage.rental.R
import com.xvantage.rental.databinding.ActivityBoardingScreenBinding
import com.xvantage.rental.databinding.ActivitySplashBinding
import com.xvantage.rental.ui.onboarding.fragment.Boarding1Fragment
import com.xvantage.rental.utils.AppPreference
import com.xvantage.rental.utils.IntentUtils

class BoardingScreenActivity : AppCompatActivity() {

    private lateinit var layoutBinding: ActivityBoardingScreenBinding
    lateinit var appPreference: AppPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        layoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_boarding_screen)
        appPreference = AppPreference(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val fragment = Boarding1Fragment()
        val frameLayoutId = R.id.fragmentContainerView
        val tag = "Boarding 1"
        IntentUtils.getInstance().navigateFromActivityToFragment(this, fragment, frameLayoutId, tag)
    }
}