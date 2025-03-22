package com.xvantage.rental.ui.manageProperty.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xvantage.rental.databinding.PropertyRoomItemsBinding
import android.annotation.SuppressLint


class ManagePropertyAdapter(
    private val context: Context,
    private val listener: OnRoomItemClickListener
) : RecyclerView.Adapter<ManagePropertyAdapter.ManagePropertyViewHolder>() {

    private var roomList: List<String> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun addItems(roomList: List<String>) {
        this.roomList = roomList
        notifyDataSetChanged()
    }

    interface OnRoomItemClickListener {
        fun onRoomClick(roomNumber: String, position: Int)
        fun onAddTenantClick(roomNumber: String, position: Int)
    }

    inner class ManagePropertyViewHolder(private val itemBinding: PropertyRoomItemsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(roomNumber: String, position: Int) {
            itemBinding.tvRoomNumber.text = roomNumber

            itemBinding.root.setOnClickListener {
                listener.onRoomClick(roomNumber, position)
            }
            itemBinding.btnAddTenant.setOnClickListener {
                listener.onAddTenantClick(roomNumber, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManagePropertyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = PropertyRoomItemsBinding.inflate(inflater, parent, false)
        return ManagePropertyViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ManagePropertyViewHolder, position: Int) {
        holder.bind(roomList[position], position)
    }

    override fun getItemCount(): Int = roomList.size
}
