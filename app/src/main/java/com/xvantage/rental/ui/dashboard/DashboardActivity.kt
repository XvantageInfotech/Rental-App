package com.xvantage.rental.ui.dashboard


import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.xvantage.rental.R
import com.xvantage.rental.databinding.ActivityDashboardBinding
import com.xvantage.rental.databinding.ToolbarLayoutBinding
import com.xvantage.rental.ui.dashboard.fragment.AddPropertyFragment
import com.xvantage.rental.ui.dashboard.fragment.DuesFragment
import com.xvantage.rental.ui.dashboard.fragment.HomeFragment

class DashboardActivity : AppCompatActivity() {
    private lateinit var layoutBinding: ActivityDashboardBinding
    private lateinit var toolbarBinding: ToolbarLayoutBinding
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        layoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)

        ViewCompat.setOnApplyWindowInsetsListener(layoutBinding.root) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                view.paddingLeft,
                view.paddingTop,
                view.paddingRight,
                systemBarsInsets.bottom
            )
            insets
        }

        drawerLayout = layoutBinding.drawerLayout

        toolbarBinding = layoutBinding.toolbar

        toolbarBinding.home.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        layoutBinding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.more_apps_tv -> Toast.makeText(this, "More Apps", Toast.LENGTH_SHORT).show()
                R.id.premium_tv -> Toast.makeText(this, "Premium", Toast.LENGTH_SHORT).show()
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
        layoutBinding.bottomNavigation.selectedItemId = R.id.home
        updateBottomNavigationIcons(R.id.home)
        layoutBinding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    updateBottomNavigationIcons(R.id.home)
                    loadFragment(HomeFragment())
                    true
                }

                R.id.profile -> {
                    updateBottomNavigationIcons(R.id.profile)
                    loadFragment(AddPropertyFragment())
                    true
                }

                R.id.settings -> {
                    updateBottomNavigationIcons(R.id.settings)
                    loadFragment(DuesFragment())
                    true
                }

                else -> false
            }
        }

        // Set the initial fragment
        if (savedInstanceState == null) {
            layoutBinding.bottomNavigation.selectedItemId = R.id.home
            loadFragment(HomeFragment())
        }
    }

    private fun updateBottomNavigationIcons(selectedItemId: Int) {
        val menu = layoutBinding.bottomNavigation.menu
        menu.findItem(R.id.home).setIcon(
            if (selectedItemId == R.id.home) R.drawable.home_nav_selected else R.drawable.home_nav_unselected
        )
        menu.findItem(R.id.profile).setIcon(
            if (selectedItemId == R.id.profile) R.drawable.add_property_nav_selected else R.drawable.add_property_nav_unselected
        )
        menu.findItem(R.id.settings).setIcon(
            if (selectedItemId == R.id.settings) R.drawable.due_nav_selected else R.drawable.due_nav_unselected
        )
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_frame, fragment)
            .commit()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}