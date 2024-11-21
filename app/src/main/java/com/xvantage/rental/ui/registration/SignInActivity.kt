package com.xvantage.rental.ui.registration

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import com.xvantage.rental.R
import com.xvantage.rental.databinding.ActivitySignInBinding
import com.xvantage.rental.ui.dashboard.DashboardActivity
import com.xvantage.rental.utils.AppPreference


class SignInActivity : AppCompatActivity() {
    private lateinit var layoutBinding: ActivitySignInBinding

    lateinit var appPreference: AppPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        layoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        intiView()
        onClickEvents()
    }

    private fun onClickEvents() {
        layoutBinding.btnSignin.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    private fun intiView() {
        appPreference = AppPreference(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}