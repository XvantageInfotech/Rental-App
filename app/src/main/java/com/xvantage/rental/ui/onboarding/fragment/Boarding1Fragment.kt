package com.xvantage.rental.ui.onboarding.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.xvantage.rental.R
import com.xvantage.rental.databinding.FragmentBoarding1Binding
import com.xvantage.rental.ui.onboarding.BoardingScreenActivity
import com.xvantage.rental.utils.AppPreference
import com.xvantage.rental.utils.IntentUtils
import kotlinx.coroutines.DelicateCoroutinesApi
@DelicateCoroutinesApi
class Boarding1Fragment : Fragment() {

    private lateinit var layoutBinding: FragmentBoarding1Binding
    private lateinit var appPreference: AppPreference
    private lateinit var landingActivity: BoardingScreenActivity
    private lateinit var viewPager: ViewPager2

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
        viewPager = landingActivity.findViewById(R.id.viewPager)
        initView()
        onClickEvents()
        return layoutBinding.root
    }

    private fun onClickEvents() {
        layoutBinding.btnNext.setOnClickListener {
            viewPager.currentItem = 1
        }
        layoutBinding.btnSkip.setOnClickListener {
            viewPager.currentItem = 3
        }
    }

    private fun initView() {
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