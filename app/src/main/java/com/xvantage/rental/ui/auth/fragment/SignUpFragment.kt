package com.xvantage.rental.ui.auth.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.xvantage.rental.databinding.FragmentSignUpBinding
import com.xvantage.rental.ui.auth.fragment.sealed.AuthScreen
import com.xvantage.rental.ui.auth.AuthViewModel
import com.xvantage.rental.utils.AppPreference
import com.xvantage.rental.utils.BaseFragment


class SignUpFragment : BaseFragment() {
    private lateinit var layoutBinding: FragmentSignUpBinding
    private lateinit var appPreference: AppPreference
    private val viewModel: AuthViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutBinding = FragmentSignUpBinding.inflate(inflater, container, false)
        return layoutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appPreference = AppPreference(requireContext())
        layoutBinding.btnNext.setOnClickListener {
            val email = /*binding.etEmail.text.toString()*/ "jh"
            val password = /*binding.etPassword.text.toString()*/ "jhh"
            viewModel.signIn(email, password)
        }


        layoutBinding.tvSignIn.setOnClickListener {
            viewModel.setCurrentScreen(AuthScreen.SignIn)
        }
    }
}