package com.xvantage.rental.ui.auth.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.xvantage.rental.databinding.FragmentForgotPasswordBinding
import com.xvantage.rental.ui.auth.AuthViewModel
import com.xvantage.rental.utils.AppPreference
import com.xvantage.rental.utils.BaseFragment

class ForgotPasswordFragment : BaseFragment() {
    private lateinit var layoutBinding: FragmentForgotPasswordBinding
    private val viewModel: AuthViewModel by activityViewModels()
    private lateinit var appPreference: AppPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutBinding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return layoutBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appPreference = AppPreference(requireContext())
        layoutBinding.btnNext.setOnClickListener {
            val email = /*binding.etEmail.text.toString()*/ "jh"
            val password = /*binding.etPassword.text.toString()*/ "jhh"
            viewModel.forgotPassword(email)
        }
    }

}