package com.xvantage.rental.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast
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
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.xvantage.rental.databinding.ToolbarLayoutBinding
import com.xvantage.rental.ui.dashboard.fragment.AddPropertyFragment
import com.xvantage.rental.ui.dashboard.fragment.DuesFragment
import com.xvantage.rental.ui.dashboard.fragment.HomeFragment

class DashboardActivity : AppCompatActivity() {
    private lateinit var layoutBinding: ActivityDashboardBinding
    private lateinit var toolbarBinding: ToolbarLayoutBinding // Custom toolbar binding
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        layoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)

        // Initialize DrawerLayout
        drawerLayout = layoutBinding.drawerLayout

        // Inflate and bind custom toolbar layout
        toolbarBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.toolbar_layout, null, false)

        // Setup custom toolbar view and its interactions
        val toolbarContainer: LinearLayout = findViewById(R.id.toolbar) // View where toolbar is included
        toolbarContainer.addView(toolbarBinding.root)

        // Handle drawer toggle with the custom toolbar
        val homeIcon = toolbarBinding.home
        homeIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Set up ActionBarDrawerToggle
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.sign_in_desc,
            R.string.sign_in_desc
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Set navigation item click listener
        val navigationView: NavigationView = layoutBinding.navigationView
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.more_apps_tv -> {
                    Toast.makeText(this, "More Apps", Toast.LENGTH_SHORT).show()
                }
                R.id.premium_tv -> {
                    Toast.makeText(this, "Premium", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Handle other items
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        val bottomNav: BottomNavigationView = layoutBinding.bottomNavigation
        bottomNav.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.profile -> {
                    loadFragment(AddPropertyFragment())
                    true
                }
                R.id.settings -> {
                    loadFragment(DuesFragment())
                    true
                }
                else -> false
            }
        }

        if (savedInstanceState == null) {
            bottomNav.selectedItemId = R.id.home
            loadFragment(HomeFragment())
        }
    }
    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_frame, fragment)
        transaction.commit()
    }
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
