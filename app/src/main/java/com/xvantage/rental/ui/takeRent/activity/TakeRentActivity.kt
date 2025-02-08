package com.xvantage.rental.ui.takeRent.activity

import android.app.LauncherActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.xvantage.rental.R
import com.xvantage.rental.utils.AppPreference
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.xvantage.rental.databinding.ActivityTakeRentBinding
import com.xvantage.rental.ui.takeRent.adapter.PropertyRoomAdapter

class TakeRentActivity : AppCompatActivity() {
    private lateinit var layoutBinding: ActivityTakeRentBinding
    lateinit var appPreference: AppPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_take_rent)
        appPreference = AppPreference(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        layoutBinding.toolbar.tvTitle.setText(R.string.take_rent)



        layoutBinding.rvPropertyList.layoutManager = LinearLayoutManager(this)

        val propertyData = listOf(
            PropertyItem("Swastik Plaza"),
            RoomItem("Room 101", "101, Swastik Plaza", 10000.0, 10000.0),
            RoomItem("Room 105", "105, Swastik Plaza", 10000.0, 10000.0),

            PropertyItem("Platinum Tower"),
            RoomItem("Bed A", "Shop 303", 10000.0, 10000.0)
        )

        layoutBinding.rvPropertyList.adapter = PropertyRoomAdapter(propertyData,this)
        onClickEvents()
    }

    private fun onClickEvents() {
        layoutBinding.toolbar.back.setOnClickListener {
            finish()
        }
    }

    data class PropertyItem(val propertyName: String) : LauncherActivity.ListItem()

    data class RoomItem(
        val roomId: String,
        val address: String,
        val monthlyRent: Double,
        val securityAmount: Double
    ) : LauncherActivity.ListItem()
}