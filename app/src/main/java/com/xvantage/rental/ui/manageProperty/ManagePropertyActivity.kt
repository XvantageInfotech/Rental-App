package com.xvantage.rental.ui.manageProperty

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.xvantage.rental.R
import com.xvantage.rental.databinding.ActivityManagePropertyBinding
import com.xvantage.rental.ui.dashboard.fragment.adapter.TenantsAdapter
import com.xvantage.rental.ui.manageProperty.adapter.ManagePropertyAdapter
import com.xvantage.rental.utils.AppPreference

class ManagePropertyActivity : AppCompatActivity() {

    private lateinit var layoutBinding: ActivityManagePropertyBinding
    lateinit var appPreference: AppPreference
    lateinit var managePropertyAdapter: ManagePropertyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_manage_property)
        appPreference = AppPreference(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        layoutBinding.toolbar.tvTitle.setText(R.string.manage_property)
        initView()
    }

    private fun initView() {

        // Set up GridLayoutManager with 2 columns
        layoutBinding.rvPropertyList.layoutManager = GridLayoutManager(this, 2)

        // Initialize and set the adapter
        managePropertyAdapter = ManagePropertyAdapter(this)
        layoutBinding.rvPropertyList.adapter = managePropertyAdapter

        // Sample data for testing
        val sampleData = listOf("101", "102", "103", "104", "105", "106")
        managePropertyAdapter.addItems(sampleData)

    }
}