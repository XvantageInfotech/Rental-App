package com.xvantage.rental.ui.addProperty.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.xvantage.rental.R
import com.xvantage.rental.databinding.ActivityPropertyDetailsBinding
import com.xvantage.rental.ui.addProperty.bmsheet.AddRoomBottomSheetFragment
import com.xvantage.rental.ui.addProperty.bmsheet.AddTenantBottomSheetFragment
import com.xvantage.rental.ui.addProperty.fragment.FinancialsFragment
import com.xvantage.rental.ui.addProperty.tempFiles.Property
import com.xvantage.rental.ui.addProperty.fragment.RoomsFragment
import com.xvantage.rental.ui.addProperty.fragment.TenantsFragment

class PropertyDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPropertyDetailsBinding
    private lateinit var property: Property
    private lateinit var tabLayoutMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this

        // Get property data from intent
        property = intent.getParcelableExtra("property") ?: Property()

        setupToolbar()
        setupPropertyDetails()
        setupViewPager()
        setupFab()
    }

    private fun setupToolbar() {
        val backButton = binding.toolbarContainer.findViewById<View>(R.id.back)

        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupPropertyDetails() {
        // Set property details in the header
        binding.tvPropertyName.text = property.name
        binding.tvPropertyAddress.text = property.address

//        // Load property image if available
//        if (property.imageUrl.isNotEmpty()) {
//            Glide.with(this)
//                .load(R.drawable.image)
//                .placeholder(R.drawable.image)
//                .centerCrop()
//                .into(binding.ivPropertyImage)
//        }
    }

    private fun setupViewPager() {
        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager

        // Create adapter for ViewPager
        val pagerAdapter = PropertyPagerAdapter(this)
        viewPager.adapter = pagerAdapter

        // Connect TabLayout with ViewPager
        tabLayoutMediator = TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Rooms"
                1 -> "Tenants"
                2 -> "Financials"
                else -> "Tab"
            }
        }
        tabLayoutMediator.attach()

        // Set tab selection listener for showing/hiding FAB based on the selected tab
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                updateFabVisibility(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun setupFab() {
        binding.fabAdd.setOnClickListener {
            // Show bottom sheet based on current tab
            when (binding.viewPager.currentItem) {
                0 -> showAddRoomBottomSheet()
                1 -> showAddTenantBottomSheet()
                else -> {} // No action for Financials tab
            }
        }

        // Set initial visibility based on default tab
        updateFabVisibility(binding.viewPager.currentItem)
    }

    private fun updateFabVisibility(tabPosition: Int) {
        // Show FAB for Rooms and Tenants tabs, hide for Financials
        binding.fabAdd.visibility = when (tabPosition) {
            0, 1 -> View.VISIBLE
            else -> View.GONE
        }
    }

    private fun showAddRoomBottomSheet() {
        val bottomSheet = AddRoomBottomSheetFragment()
        bottomSheet.setOnRoomAddedListener { room ->
            // Notify rooms fragment about the new room
            val roomsFragment = supportFragmentManager.fragments.find { it is RoomsFragment } as? RoomsFragment
            roomsFragment?.addRoom(room)
        }
        bottomSheet.show(supportFragmentManager, "AddRoomBottomSheet")
    }

    private fun showAddTenantBottomSheet() {
        val bottomSheet = AddTenantBottomSheetFragment()
        bottomSheet.setOnTenantAddedListener { tenant ->
            // Notify tenants fragment about the new tenant
            val tenantsFragment = supportFragmentManager.fragments.find { it is TenantsFragment } as? TenantsFragment
            tenantsFragment?.addRoom(tenant)
        }
        bottomSheet.show(supportFragmentManager, "AddTenantBottomSheet")
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        // Detach TabLayoutMediator to prevent memory leaks
        if (::tabLayoutMediator.isInitialized) {
            tabLayoutMediator.detach()
        }
    }

    /**
     * ViewPager adapter for property tabs
     */
    private inner class PropertyPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> RoomsFragment.Companion.newInstance(property.id)
                1 -> TenantsFragment.Companion.newInstance(property.id)
                2 -> FinancialsFragment.Companion.newInstance(property.id)
                else -> Fragment()
            }
        }
    }
}