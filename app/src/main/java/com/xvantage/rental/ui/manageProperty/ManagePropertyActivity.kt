package com.xvantage.rental.ui.manageProperty

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.core.view.WindowCompat
import com.xvantage.rental.R
import com.xvantage.rental.databinding.ActivityManagePropertyBinding
import com.xvantage.rental.ui.addTenant.AddTenantActivity
import com.xvantage.rental.ui.manageProperty.adapter.ManagePropertyAdapter
import com.xvantage.rental.utils.AppPreference
import com.xvantage.rental.utils.CommonFunction


class ManagePropertyActivity : AppCompatActivity(), ManagePropertyAdapter.OnRoomItemClickListener {

    private lateinit var binding: ActivityManagePropertyBinding
    private lateinit var appPreference: AppPreference
    private lateinit var managePropertyAdapter: ManagePropertyAdapter

    // private val viewModel: ManagePropertyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage_property)
        appPreference = AppPreference(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Set toolbar title
        binding.toolbar.tvTitle.setText(R.string.manage_property)

        initViews()
    }

    /**
     * Initialize RecyclerView and static data.
     */
    private fun initViews() {
        binding.rvPropertyList.layoutManager = GridLayoutManager(this, 2)
        managePropertyAdapter = ManagePropertyAdapter(this, this)
        binding.rvPropertyList.adapter = managePropertyAdapter

        // Static sample data for now
        val sampleData = listOf("101", "102", "103", "104", "105", "106")
        managePropertyAdapter.addItems(sampleData)

        binding.toolbar.back.setOnClickListener { onBackPressed() }
    }

    override fun onRoomClick(roomNumber: String, position: Int) {
        CommonFunction().toast(this, "$roomNumber Clicked")
    }

    override fun onAddTenantClick(roomNumber: String, position: Int) {
        CommonFunction().navigation(this, AddTenantActivity::class.java)
    }
}
