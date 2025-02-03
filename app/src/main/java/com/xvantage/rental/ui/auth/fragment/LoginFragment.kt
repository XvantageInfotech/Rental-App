package com.xvantage.rental.ui.auth.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.xvantage.rental.databinding.FragmentLoginBinding
import com.xvantage.rental.ui.auth.AuthScreen
import com.xvantage.rental.ui.auth.AuthViewModel
import com.xvantage.rental.ui.auth.GoogleAuthClient
import com.xvantage.rental.ui.auth.SignInResult
import com.xvantage.rental.ui.auth.SignInResult.Cancelled
import com.xvantage.rental.ui.auth.SignInResult.ErrorTypeCredentials
import com.xvantage.rental.ui.auth.SignInResult.Failure
import com.xvantage.rental.ui.auth.SignInResult.NoCredentials
import com.xvantage.rental.ui.auth.SignInResult.Success
import com.xvantage.rental.utils.AppPreference
import com.xvantage.rental.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment() {
    private lateinit var layoutBinding: FragmentLoginBinding
    private lateinit var appPreference: AppPreference
    private val viewModel: AuthViewModel by activityViewModels()
    lateinit var googleAuthClient: GoogleAuthClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutBinding = FragmentLoginBinding.inflate(inflater, container, false)
        return layoutBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appPreference = AppPreference(requireContext())
        googleAuthClient = GoogleAuthClient(requireActivity())

        layoutBinding.btnSignin.setOnClickListener {
            val email = /*binding.etEmail.text.toString()*/ "jh"
            val password = /*binding.etPassword.text.toString()*/ "jhh"
            viewModel.signIn(email, password)
        }

        layoutBinding.forgotPassword.setOnClickListener {
            viewModel.setCurrentScreen(AuthScreen.ForgotPassword)
        }
        layoutBinding.signupGoogleBtn.setOnClickListener {
            lifecycleScope.launch {
                when (val result = googleAuthClient.signIn()) {
                    is SignInResult.Cancelled -> {
                        Log.d("LoginFragment", "Sign-in cancelled")
                        Toast.makeText(context, "Sign-in cancelled", Toast.LENGTH_SHORT).show()
                    }
                    is SignInResult.ErrorTypeCredentials -> {
                        Log.d("LoginFragment", "Error with credentials")
                        Toast.makeText(context, "Error with credentials", Toast.LENGTH_SHORT).show()
                    }
                    is SignInResult.Failure -> {
                        Log.d("LoginFragment", "Sign-in failed")
                        Toast.makeText(context, "Sign-in failed", Toast.LENGTH_SHORT).show()
                    }
                    is SignInResult.NoCredentials -> {
                        Log.d("LoginFragment", "No credentials found")
                        Toast.makeText(context, "No credentials found", Toast.LENGTH_SHORT).show()
                    }
                    is SignInResult.Success -> {
                        Log.d("LoginFragment", "Sign-in successful")
                        Toast.makeText(context, "Sign-in successful", Toast.LENGTH_SHORT).show()
                        // Handle successful sign-in, e.g., navigate to another screen
                    }
                }
            }
        }

        layoutBinding.tvSignUp.setOnClickListener {
            viewModel.setCurrentScreen(AuthScreen.SignUp)
        }
//        viewModel.init()


    }
}