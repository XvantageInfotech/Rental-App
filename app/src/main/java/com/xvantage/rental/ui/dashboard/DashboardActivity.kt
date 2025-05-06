package com.xvantage.rental.ui.dashboard

import android.os.Bundle
import android.view.View
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
import com.xvantage.rental.ui.addProperty.activity.AddPropertyActivity
import com.xvantage.rental.ui.dashboard.fragment.DuesFragment
import com.xvantage.rental.ui.dashboard.fragment.HomeFragment
import com.xvantage.rental.utils.CommonFunction

class DashboardActivity : AppCompatActivity() {
    private lateinit var layoutBinding: ActivityDashboardBinding
    private lateinit var toolbarBinding: ToolbarLayoutBinding
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupWindow()
        setupViews()
        setupToolbar()
        setupNavigationDrawer()
        setupBottomNavigation()
        initializeDefaultFragment(savedInstanceState)
    }

    private fun setupWindow() {
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        layoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        setupWindowInsets()
    }

    private fun setupWindowInsets() {
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
    }

    private fun setupViews() {
        drawerLayout = layoutBinding.drawerLayout
        toolbarBinding = layoutBinding.toolbar
    }

    private fun setupToolbar() {
        with(toolbarBinding) {
            home.visibility = View.VISIBLE
            search.visibility = View.VISIBLE
            setting.visibility = View.VISIBLE
            btnSave.visibility = View.GONE
            back.visibility = View.GONE

            home.setOnClickListener { toggleDrawer() }
        }
    }

    private fun setupNavigationDrawer() {
        with(layoutBinding.navigationView) {
            findViewById<View>(R.id.more_apps_tv)?.setOnClickListener {
                showToast("More Apps")
                closeDrawer()
            }

            findViewById<View>(R.id.premium_tv)?.setOnClickListener {
                showToast("Premium")
                closeDrawer()
            }

            findViewById<View>(R.id.rate_us_tv)?.setOnClickListener {
                CommonFunction().showRatingDialog(this@DashboardActivity)
                closeDrawer()
            }
        }
    }

    private fun setupBottomNavigation() {
        with(layoutBinding.bottomNavigation) {
            selectedItemId = R.id.home
            updateBottomNavigationIcons(R.id.home)
            setOnNavigationItemSelectedListener { menuItem ->
                handleBottomNavigationItemSelected(menuItem.itemId)
            }
        }
    }

    private fun handleBottomNavigationItemSelected(itemId: Int): Boolean {
        updateBottomNavigationIcons(itemId)
        return when (itemId) {
            R.id.home -> {
                loadFragment(HomeFragment())
                true
            }
            R.id.property -> {
                CommonFunction().navigation(this, AddPropertyActivity::class.java)
                true
            }
            R.id.settings -> {
                loadFragment(DuesFragment())
                true
            }
            else -> false
        }
    }

    private fun initializeDefaultFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            layoutBinding.bottomNavigation.selectedItemId = R.id.home
            loadFragment(HomeFragment())
        }
    }

    private fun updateBottomNavigationIcons(selectedItemId: Int) {
        val menu = layoutBinding.bottomNavigation.menu
        menu.findItem(R.id.home).setIcon(
            if (selectedItemId == R.id.home) R.drawable.home_nav_selected 
            else R.drawable.home_nav_unselected
        )
        menu.findItem(R.id.settings).setIcon(
            if (selectedItemId == R.id.settings) R.drawable.due_nav_selected 
            else R.drawable.due_nav_unselected
        )
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_frame, fragment)
            .commit()
    }

    private fun toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeDrawer()
        } else {
            openDrawer()
        }
    }

    private fun openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START)
    }

    private fun closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeDrawer()
        } else {
            super.onBackPressed()
        }
    }
}