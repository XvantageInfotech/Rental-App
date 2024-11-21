package com.xvantage.rental.ui.onboarding.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.xvantage.rental.R
import com.xvantage.rental.databinding.FragmentBoarding1Binding
import com.xvantage.rental.ui.onboarding.BoardingScreenActivity
import com.xvantage.rental.utils.AppPreference
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import java.util.Locale

@DelicateCoroutinesApi
class Boarding1Fragment : Fragment() {

    private lateinit var layoutBinding: FragmentBoarding1Binding
    lateinit var appPreference: AppPreference
    private lateinit var landingActivity: BoardingScreenActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        landingActivity = context as BoardingScreenActivity
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        layoutBinding = FragmentBoarding1Binding.inflate(inflater, container, false)
        intiView()
        onClickEvents()
        return layoutBinding.root
    }


    private fun onClickEvents() {

        layoutBinding.btnNext.setOnClickListener {
        }

    }

    private fun intiView() {
        appPreference = AppPreference(requireContext())

    }


    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
    }

    companion object {
        // Remote Config keys
        const val SERVER_KEY = "PushNotificationServerKey"
    }
}
