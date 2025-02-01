package com.xvantage.rental.ui.auth.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xvantage.rental.R
import com.xvantage.rental.databinding.FragmentBoarding1Binding
import com.xvantage.rental.databinding.FragmentLoginBinding
import com.xvantage.rental.utils.AppPreference
import com.xvantage.rental.utils.BaseFragment

class LoginFragment : BaseFragment() {
    private lateinit var layoutBinding: FragmentLoginBinding
    private lateinit var appPreference: AppPreference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        layoutBinding = FragmentLoginBinding.inflate(inflater, container, false)
        return layoutBinding.root
    }

}