package com.xvantage.rental.ui.onboarding.fragment.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.xvantage.rental.ui.onboarding.fragment.Boarding1Fragment
import com.xvantage.rental.ui.onboarding.fragment.Boarding2Fragment
import com.xvantage.rental.ui.onboarding.fragment.Boarding3Fragment
import com.xvantage.rental.ui.onboarding.fragment.Boarding4Fragment
import androidx.viewpager2.widget.ViewPager2

class BoardingPagerAdapter(activity: AppCompatActivity, private val viewPager: ViewPager2) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> Boarding1Fragment(viewPager)
            1 -> Boarding2Fragment(viewPager)
            2 -> Boarding3Fragment(viewPager)
            3 -> Boarding4Fragment(viewPager)
            else -> Boarding1Fragment(viewPager)
        }
    }
}
