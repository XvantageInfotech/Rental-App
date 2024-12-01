package com.xvantage.rental.ui.dashboard.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.xvantage.rental.R
import com.xvantage.rental.databinding.FragmentBoarding1Binding
import com.xvantage.rental.databinding.FragmentHomeBinding
import com.xvantage.rental.ui.dashboard.fragment.adapter.PropertiesAdapter
import com.xvantage.rental.ui.onboarding.BoardingScreenActivity
import com.xvantage.rental.utils.AppPreference
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class Boarding1Fragment(private val viewPager: ViewPager2) : Fragment() {

    private lateinit var layoutBinding: FragmentHomeBinding
    lateinit var appPreference: AppPreference
    lateinit var propertiesAdapter: PropertiesAdapter

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
        layoutBinding = FragmentHomeBinding.inflate(inflater, container, false)
        intiView()
        onClickEvents()
        return layoutBinding.root
    }

    private fun onClickEvents() {
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
