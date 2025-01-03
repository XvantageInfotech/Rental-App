package com.xvantage.rental.ui.onboarding

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.xvantage.rental.databinding.ActivityBoardingScreenBinding
import com.xvantage.rental.ui.onboarding.fragment.adapter.BoardingPagerAdapter
import com.xvantage.rental.utils.AppPreference

class BoardingScreenActivity : AppCompatActivity() {

    private lateinit var layoutBinding: ActivityBoardingScreenBinding
    private lateinit var appPreference: AppPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        layoutBinding = ActivityBoardingScreenBinding.inflate(layoutInflater)
        setContentView(layoutBinding.root)
        appPreference = AppPreference(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val viewPager = layoutBinding.viewPager
        val adapter = BoardingPagerAdapter(this@BoardingScreenActivity)
        viewPager.adapter = adapter
    }

    override fun onBackPressed() {
        // Handle back press
        super.onBackPressed()
    }
}