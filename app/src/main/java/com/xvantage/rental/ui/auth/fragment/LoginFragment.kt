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
import com.xvantage.rental.network.request.auth.GoogleLoginRequest
import com.xvantage.rental.ui.auth.fragment.sealed.AuthScreen
import com.xvantage.rental.ui.auth.AuthViewModel
import com.xvantage.rental.ui.auth.GoogleAuthClient
import com.xvantage.rental.ui.auth.fragment.sealed.SignInResult
import com.xvantage.rental.utils.AppPreference
import com.xvantage.rental.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
            val email = layoutBinding.etSignUpEmail.text.toString().trim()
            val password = layoutBinding.etSignUpPassword.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.signIn(email, password)
            } else {
                Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
        layoutBinding.forgotPassword.setOnClickListener {
            viewModel.setCurrentScreen(AuthScreen.ForgotPassword)
        }
        layoutBinding.signupGoogleBtn.setOnClickListener {
            lifecycleScope.launch {
                when (val result = googleAuthClient.signIn()) {
                    is SignInResult.Cancelled -> {
                        Log.d("SignUpFragment", "Google sign-up cancelled")
                        Toast.makeText(context, "Google sign-up cancelled", Toast.LENGTH_SHORT).show()
                    }
                    is SignInResult.ErrorTypeCredentials -> {
                        Log.d("SignUpFragment", "Error with Google credentials")
                        Toast.makeText(context, "Error with Google credentials", Toast.LENGTH_SHORT).show()
                    }
                    is SignInResult.Failure -> {
                        Log.d("SignUpFragment", "Google sign-up failed")
                        Toast.makeText(context, "Google sign-up failed", Toast.LENGTH_SHORT).show()
                    }
                    is SignInResult.NoCredentials -> {
                        Log.d("SignUpFragment", "No Google credentials found")
                        Toast.makeText(context, "No Google credentials found", Toast.LENGTH_SHORT).show()
                    }
                    is SignInResult.Success -> {
                        Log.d("SignUpFragment", "Google sign-up successful")
                        Toast.makeText(context, "Google sign-up successful", Toast.LENGTH_SHORT).show()
                        val email = result.email
                        if (email.isNullOrEmpty()) {
                            Toast.makeText(context, "Google sign-in did not return an email", Toast.LENGTH_SHORT).show()
                            return@launch
                        }

                        val googleData = GoogleLoginRequest.GoogleData(
                            email = email,
                            firstName = result.username,
                            profilePic = result.avatarUrl,
                            socialId = result.id,
                            deviceToken = result.idToken,
                            deviceType = "android"
                        )
                        val googleLoginRequest = GoogleLoginRequest(
                            googleData = googleData
                        )
                        viewModel.signUpWithGoogle(googleLoginRequest)
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