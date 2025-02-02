package com.xvantage.rental.ui.auth.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xvantage.rental.R
import com.xvantage.rental.databinding.FragmentForgotPasswordBinding
import com.xvantage.rental.databinding.FragmentLoginBinding

class ForgotPasswordFragment : Fragment() {
    private lateinit var layoutBinding: FragmentForgotPasswordBinding

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


}