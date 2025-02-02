package com.xvantage.rental.ui.auth.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xvantage.rental.R
import com.xvantage.rental.databinding.FragmentSignUpBinding
import com.xvantage.rental.databinding.FragmentVerifyOtpBinding
import com.xvantage.rental.utils.AppPreference


class VerifyOtpFragment : Fragment() {
    private lateinit var layoutBinding: FragmentVerifyOtpBinding
    private lateinit var appPreference: AppPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutBinding = FragmentVerifyOtpBinding.inflate(inflater, container, false)
        return layoutBinding.root

    }

}