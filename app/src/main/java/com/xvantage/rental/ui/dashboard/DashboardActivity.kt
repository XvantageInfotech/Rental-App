package com.xvantage.rental.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
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
    lateinit var appPreference: AppPreference
    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        // Set up data binding for the activity layout
        // Set up data binding for the activity layout
        layoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)


        drawerLayout = layoutBinding.drawerLayout

        // Inflate the custom toolbar layout
        toolbarBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.toolbar_layout, null, false)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // ActionBarDrawerToggle to handle drawer opening and closing
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.sign_in_desc,  // String for opening the drawer
            R.string.sign_in_desc  // String for closing the drawer
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Handle toolbar click to open drawer
        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Handle navigation item clicks
        val navigationView: NavigationView = layoutBinding.navigationView
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.more_apps_tv -> {
                    Toast.makeText(this,"More Apps", Toast.LENGTH_SHORT)
                }
                R.id.premium_tv -> {
                    Toast.makeText(this,"Premium", Toast.LENGTH_SHORT)
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

// Handle bottom navigation selection
        val bottomNav: BottomNavigationView = layoutBinding.bottomNavigation
        bottomNav.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.profile -> {
                    loadFragment(AddPropertyFragment())  // Load Profile fragment
                    true
                }
                R.id.settings -> {
                    loadFragment(DuesFragment())  // Load Settings fragment
                    true
                }
                else -> false
            }
        }

        // Load the default fragment when activity starts
        if (savedInstanceState == null) {
            bottomNav.selectedItemId = R.id.home // Ensure home is selected by default
            loadFragment(HomeFragment())
        }
    }
    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_frame, fragment)
        transaction.commit()
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
