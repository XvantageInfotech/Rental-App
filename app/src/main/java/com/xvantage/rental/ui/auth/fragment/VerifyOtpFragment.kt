package com.xvantage.rental.ui.auth.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.xvantage.rental.databinding.FragmentVerifyOtpBinding
import com.xvantage.rental.ui.auth.AuthViewModel
import com.xvantage.rental.utils.AppPreference
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class VerifyOtpFragment : Fragment() {
    private lateinit var layoutBinding: FragmentVerifyOtpBinding
    private lateinit var appPreference: AppPreference
    private val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        layoutBinding = FragmentVerifyOtpBinding.inflate(inflater, container, false)
        return layoutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appPreference = AppPreference(requireContext())

        // Retrieve the email passed via the bundle
        val email = arguments?.getString("email") ?: run {
            Toast.makeText(context, "Email not provided", Toast.LENGTH_SHORT).show()
            return
        }

        layoutBinding.otpView.setOnFinishListener { otp ->
            viewModel.verifyOtp(email, otp, "email")
        }

        layoutBinding.btnNext.setOnClickListener {
            val otp = layoutBinding.otpView.getStringFromFields()
            viewModel.verifyOtp(email, otp, "email")
        }
    }
}
