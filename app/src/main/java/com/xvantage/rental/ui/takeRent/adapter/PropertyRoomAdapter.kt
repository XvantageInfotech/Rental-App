package com.xvantage.rental.ui.takeRent.adapter

import android.app.LauncherActivity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.xvantage.rental.databinding.ItemPropertyCardBinding
import com.xvantage.rental.databinding.ItemPropertyHeaderBinding
import com.xvantage.rental.ui.takeRent.activity.ReceivePaymentActivity
import com.xvantage.rental.ui.takeRent.activity.TakeRentActivity

class PropertyRoomAdapter(private val items: List<LauncherActivity.ListItem>,val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_PROPERTY = 0
        private const val TYPE_ROOM = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is TakeRentActivity.PropertyItem -> TYPE_PROPERTY
            is TakeRentActivity.RoomItem -> TYPE_ROOM
            else -> {
                throw IllegalArgumentException("Invalid type of data $position")}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_PROPERTY -> {
                val binding = ItemPropertyHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                PropertyViewHolder(binding)
            }
            TYPE_ROOM -> {
                val binding = ItemPropertyCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                RoomViewHolder(binding, context)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is TakeRentActivity.PropertyItem -> (holder as PropertyViewHolder).bind(item)
            is TakeRentActivity.RoomItem -> (holder as RoomViewHolder).bind(item)
        }
    }

    override fun getItemCount(): Int = items.size

    class PropertyViewHolder(private val binding: ItemPropertyHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(property: TakeRentActivity.PropertyItem) {
            binding.propertyTitle.text = property.propertyName
        }
    }

    class RoomViewHolder(private val binding: ItemPropertyCardBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bind(room: TakeRentActivity.RoomItem) {
            binding.roomId.text = room.roomId
            binding.address.text = room.address
            binding.monthlyRent.text = "₹${room.monthlyRent}"
            binding.tvDepositAmount.text = "₹${room.securityAmount}"
            binding.btnRcvPayment.setOnClickListener {
                startActivity(context, Intent(context, ReceivePaymentActivity::class.java), null)
            }
        }
    }
}