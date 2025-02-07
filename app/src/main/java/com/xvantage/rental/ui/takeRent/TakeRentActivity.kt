package com.xvantage.rental.ui.takeRent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.xvantage.rental.R
import com.xvantage.rental.utils.AppPreference
import androidx.core.view.WindowCompat
import com.xvantage.rental.databinding.ActivityTakeRentBinding
import com.xvantage.rental.utils.CommonFunction

class TakeRentActivity : AppCompatActivity() {
    private lateinit var layoutBinding: ActivityTakeRentBinding
    lateinit var appPreference: AppPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_take_rent)
        appPreference = AppPreference(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        layoutBinding.toolbar.tvTitle.setText(R.string.take_rent)
        onClickEvents()
    }

    private fun onClickEvents() {
        layoutBinding.toolbar.back.setOnClickListener {
            finish()
        }
    }
}