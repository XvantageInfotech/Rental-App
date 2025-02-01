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
import com.xvantage.rental.ui.addTenant.AddTenantActivity
import com.xvantage.rental.ui.dashboard.fragment.adapter.TenantsAdapter
import com.xvantage.rental.ui.manageProperty.adapter.ManagePropertyAdapter
import com.xvantage.rental.utils.AppPreference
import com.xvantage.rental.utils.CommonFunction

class ManagePropertyActivity : AppCompatActivity(), ManagePropertyAdapter.OnRoomItemClickListener {

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

        layoutBinding.rvPropertyList.layoutManager = GridLayoutManager(this, 2)

        managePropertyAdapter = ManagePropertyAdapter(this,this)
        layoutBinding.rvPropertyList.adapter = managePropertyAdapter

        val sampleData = listOf("101", "102", "103", "104", "105", "106")
        managePropertyAdapter.addItems(sampleData)

        layoutBinding.toolbar.back.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onRoomClick(roomNumber: String, position: Int) {
        CommonFunction().toast(this,"$roomNumber Clicked")
    }

    override fun onAddTenantClick(roomNumber: String, position: Int) {
        CommonFunction().navigation(this,AddTenantActivity::class.java)
    }
}