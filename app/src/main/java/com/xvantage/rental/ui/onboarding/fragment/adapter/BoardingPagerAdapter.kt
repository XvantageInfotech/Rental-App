package com.xvantage.rental.ui.onboarding.fragment.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.xvantage.rental.ui.onboarding.fragment.Boarding1Fragment
import com.xvantage.rental.ui.onboarding.fragment.Boarding2Fragment
import com.xvantage.rental.ui.onboarding.fragment.Boarding3Fragment
import com.xvantage.rental.ui.onboarding.fragment.Boarding4Fragment
import kotlinx.coroutines.DelicateCoroutinesApi


class BoardingPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    companion object {
        private const val NUM_PAGES = 4
    }

    override fun getItemCount(): Int = NUM_PAGES

    @OptIn(DelicateCoroutinesApi::class)
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> Boarding1Fragment()
            1 -> Boarding2Fragment()
            2 -> Boarding3Fragment()
            3 -> Boarding4Fragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}