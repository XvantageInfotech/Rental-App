package com.xvantage.rental.ui.addProperty

/**
 * Project: Rental App By XV Team
 * Author: Mujammil x Vipul x XV Team
 * Date:  24/04/25
 * <p>
 * Licensed under the Apache License, Version 2.0. See LICENSE file for terms.
 */

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xvantage.rental.databinding.ItemRoomBinding
import java.text.NumberFormat
import java.util.Locale

class RoomAdapter(private val onRoomClicked: (Room) -> Unit) :
    ListAdapter<Room, RoomAdapter.RoomViewHolder>(RoomDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val binding = ItemRoomBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RoomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class RoomViewHolder(private val binding: ItemRoomBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onRoomClicked(getItem(position))
                }
            }
        }

        fun bind(room: Room) {
            // Set room number in circle
            binding.tvRoomNumberCircle.text = room.number.take(2)

            // Set room details
            binding.tvRoomName.text = "Room ${room.number}"
            binding.tvRoomType.text = room.type

            // Format and display rent
            val formattedRent = NumberFormat.getCurrencyInstance(Locale.getDefault())
                .format(room.rent)
            binding.tvRoomRent.text = "$formattedRent/month"

            // Set status badge
            binding.tvStatusBadge.text = if (room.isOccupied) "Occupied" else "Vacant"

            // Set badge background color based on occupancy status
            val badgeColor = if (room.isOccupied) Color.parseColor("#4CAF50") else Color.parseColor("#FF9800")
            binding.tvStatusBadge.backgroundTintList = android.content.res.ColorStateList.valueOf(badgeColor)
        }
    }

    private class RoomDiffCallback : DiffUtil.ItemCallback<Room>() {
        override fun areItemsTheSame(oldItem: Room, newItem: Room): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Room, newItem: Room): Boolean {
            return oldItem == newItem
        }
    }
}