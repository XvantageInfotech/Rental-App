package com.xvantage.rental.ui.onboarding.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xvantage.rental.R
import com.xvantage.rental.databinding.FragmentBoarding2Binding
import com.xvantage.rental.ui.onboarding.BoardingScreenActivity
import com.xvantage.rental.utils.AppPreference
import com.xvantage.rental.utils.IntentUtils
import kotlinx.coroutines.DelicateCoroutinesApi


@DelicateCoroutinesApi
class Boarding2Fragment : Fragment() {

    private lateinit var layoutBinding: FragmentBoarding2Binding
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
        layoutBinding = FragmentBoarding2Binding.inflate(inflater, container, false)
        intiView()
        onClickEvents()
        return layoutBinding.root
    }


    private fun onClickEvents() {
        layoutBinding.btnNext.setOnClickListener {
            IntentUtils.getInstance().navigateFromFragmentToFragment(
                currentFragment = this,
                newFragment = Boarding3Fragment(),
                containerId = R.id.fragmentContainerView,
                tag = "Boarding3Fragment"
            )
        }
        layoutBinding.btnSkip.setOnClickListener {
            IntentUtils.getInstance().navigateFromFragmentToFragment(
                currentFragment = this,
                newFragment = Boarding4Fragment(),
                containerId = R.id.fragmentContainerView,
                tag = "Boarding4Fragment"
            )
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