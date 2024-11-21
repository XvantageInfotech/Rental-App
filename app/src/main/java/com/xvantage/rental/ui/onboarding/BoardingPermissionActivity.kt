package com.xvantage.rental.ui.onboarding

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import com.xvantage.rental.R
import com.xvantage.rental.databinding.ActivityBoardingPermissionBinding
import com.xvantage.rental.utils.AppPreference
import com.xvantage.rental.utils.libs.toggle.SwitchButton

class BoardingPermissionActivity : AppCompatActivity() {
    private lateinit var layoutBinding: ActivityBoardingPermissionBinding
    lateinit var appPreference: AppPreference
    private var isMediaToggle: Boolean = false
    private var isContactToggle: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        layoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_boarding_permission)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        intiView()
        onClickEvents()
    }

    private fun onClickEvents() {
        layoutBinding.mediaToggle.isChecked = isMediaToggle
        layoutBinding.contactToggle.isChecked = isContactToggle

        layoutBinding.mediaToggle.setOnCheckedChangeListener(object :
            SwitchButton.OnCheckedChangeListener {
            override fun onCheckedChanged(view: SwitchButton?, isChecked: Boolean) {
                isMediaToggle = isChecked
            }
        })

        layoutBinding.contactToggle.setOnCheckedChangeListener(object :
            SwitchButton.OnCheckedChangeListener {
            override fun onCheckedChanged(view: SwitchButton?, isChecked: Boolean) {
                isContactToggle = isChecked
            }
        })
    }

    private fun intiView() {
        appPreference = AppPreference(this)

    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
    }
}