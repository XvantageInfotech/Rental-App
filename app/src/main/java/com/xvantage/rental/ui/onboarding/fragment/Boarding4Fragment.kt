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
import com.xvantage.rental.databinding.FragmentBoarding4Binding
import com.xvantage.rental.ui.onboarding.BoardingPermissionActivity
import com.xvantage.rental.ui.onboarding.BoardingScreenActivity
import com.xvantage.rental.utils.AppPreference
import com.xvantage.rental.utils.IntentUtils
import kotlinx.coroutines.DelicateCoroutinesApi
@DelicateCoroutinesApi
class Boarding4Fragment : Fragment() {

    private lateinit var layoutBinding: FragmentBoarding4Binding
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
        layoutBinding = FragmentBoarding4Binding.inflate(inflater, container, false)
        viewPager = landingActivity.findViewById(R.id.viewPager)
        initView()
        onClickEvents()
        return layoutBinding.root
    }

    private fun onClickEvents() {
        val bundle = Bundle().apply {
            putString("key", "value") // Example
        }
        val options = Bundle()
        IntentUtils.getInstance().navigateToNextActivity(
            activity = requireActivity(),
            fragment = this,
            classActivity = BoardingPermissionActivity::class.java,
            bundle = bundle,
            options = options
        )
    }

    private fun initView() {
        appPreference = AppPreference(requireContext())
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
    }

    companion object {
        const val SERVER_KEY = "PushNotificationServerKey"
    }
}