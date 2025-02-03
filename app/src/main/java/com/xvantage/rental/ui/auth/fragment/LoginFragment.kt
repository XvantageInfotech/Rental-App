package com.xvantage.rental.ui.auth.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.xvantage.rental.databinding.FragmentLoginBinding
import com.xvantage.rental.ui.auth.AuthScreen
import com.xvantage.rental.ui.auth.AuthViewModel
import com.xvantage.rental.utils.AppPreference
import com.xvantage.rental.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment() {
    private lateinit var layoutBinding: FragmentLoginBinding
    private lateinit var appPreference: AppPreference
    private val viewModel: AuthViewModel by viewModels()


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
        layoutBinding.btnSignin.setOnClickListener {
            val email = /*binding.etEmail.text.toString()*/ "jh"
            val password = /*binding.etPassword.text.toString()*/ "jhh"
            viewModel.signIn(email, password)
        }

        layoutBinding.forgotPassword.setOnClickListener {
            viewModel.setCurrentScreen(AuthScreen.ForgotPassword)
        }

        layoutBinding.tvSignUp.setOnClickListener {
            viewModel.setCurrentScreen(AuthScreen.SignUp)
        }
//        viewModel.init()


    }
}