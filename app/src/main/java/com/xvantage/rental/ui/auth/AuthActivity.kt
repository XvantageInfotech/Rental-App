package com.xvantage.rental.ui.auth

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.xvantage.rental.R
import com.xvantage.rental.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.auth_fragments_container) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.signInFragment -> showLoading(false)
                R.id.signUpFragment -> showLoading(false)
                R.id.verifyOtpFragment -> showLoading(false)
                R.id.forgotPasswordFragment -> showLoading(false)
                R.id.dashboardActivity -> showLoading(false)
                else -> showLoading(true)
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }


}
