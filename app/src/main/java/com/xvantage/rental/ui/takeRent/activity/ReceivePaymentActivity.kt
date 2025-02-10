package com.xvantage.rental.ui.takeRent.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.xvantage.rental.R
import com.xvantage.rental.databinding.ActivityReceivePaymentBinding
import com.xvantage.rental.databinding.ActivityTakeRentBinding
import com.xvantage.rental.utils.AppPreference

class ReceivePaymentActivity : AppCompatActivity() {
    private lateinit var layoutBinding: ActivityReceivePaymentBinding
    lateinit var appPreference: AppPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_receive_payment)
        appPreference = AppPreference(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        layoutBinding.toolbar.tvTitle.setText(R.string.receive_payment)
        layoutBinding.toolbar.back.setOnClickListener {
            onBackPressed()
        }
    }
}