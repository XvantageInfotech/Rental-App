package com.xvantage.rental.ui.dashboard

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.xvantage.rental.R
import com.xvantage.rental.databinding.ActivityDashboardBinding
import com.xvantage.rental.databinding.ActivitySignInBinding
import com.xvantage.rental.utils.AppPreference

import androidx.activity.enableEdgeToEdge
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.xvantage.rental.databinding.ToolbarLayoutBinding

class DashboardActivity : AppCompatActivity() {
    private lateinit var layoutBinding: ActivityDashboardBinding
    private lateinit var toolbarBinding: ToolbarLayoutBinding
    lateinit var appPreference: AppPreference
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Set up data binding
        layoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Initialize the DrawerLayout and Toolbar
        drawerLayout = layoutBinding.drawerLayout
        val toolbar: Toolbar = layoutBinding.toolbar

        setSupportActionBar(toolbar)

        // Set up ActionBarDrawerToggle
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.sign_in_desc,
            R.string.sign_in_desc
        )

        // Sync the toggle state with the drawer layout
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Handle navigation item clicks
        val navigationView: NavigationView = layoutBinding.navigationView
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    // Handle first item click
                }
                R.id.profile -> {
                    // Handle second item click
                }
                else -> {
                    // Handle other items
                }
            }
            // Close the drawer after item is clicked
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Bottom Navigation setup
        val bottomNav: BottomNavigationView = layoutBinding.bottomNavigation
        bottomNav.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    // Handle bottom item one click
                    true
                }
                R.id.profile -> {
                    // Handle bottom item two click
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    override fun onBackPressed() {
        // Close the navigation drawer if open, else handle back press normally
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
