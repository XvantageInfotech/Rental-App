package com.xvantage.rental.ui.registration

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import com.xvantage.rental.R
import com.xvantage.rental.databinding.ActivitySignInBinding
import com.xvantage.rental.ui.dashboard.DashboardActivity
import com.xvantage.rental.utils.AppPreference
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class SignInActivity : AppCompatActivity() {
    private lateinit var layoutBinding: ActivitySignInBinding

    private val viewModel: SignInViewModel by viewModels()


    lateinit var appPreference: AppPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        layoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        WindowCompat.setDecorFitsSystemWindows(window, false)

       /* viewModel.loginState.onEach { state ->
            when (state) {
                is ResultWrapper.Loading -> showLoading()
                is ResultWrapper.Success -> handleSuccess(state.data)
                is ResultWrapper.Error -> showError(state.message)
            }
        }.launchIn(lifecycleScope)
        */
        intiView()
        onClickEvents()
    }
    /*private fun login() {
        val email = "example@example.com"
        val password = "password123"
        viewModel.login(email, password)
    }*/
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