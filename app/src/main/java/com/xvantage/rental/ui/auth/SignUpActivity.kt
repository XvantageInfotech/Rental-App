package com.xvantage.rental.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import com.xvantage.rental.R
import com.xvantage.rental.databinding.ActivitySignUpBinding
import com.xvantage.rental.utils.AppPreference




class SignUpActivity : AppCompatActivity() {
    private lateinit var layoutBinding: ActivitySignUpBinding
    lateinit var appPreference: AppPreference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        layoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        intiView()

        onClickEvents()
    }


    private fun onClickEvents() {
        layoutBinding.btnNext.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    private fun intiView() {
        appPreference = AppPreference(this)
    }
}