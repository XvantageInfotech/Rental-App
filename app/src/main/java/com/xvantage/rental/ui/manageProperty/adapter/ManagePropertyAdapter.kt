package com.xvantage.rental.ui.manageProperty.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xvantage.rental.databinding.PropertyRoomItemsBinding

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

        fun setData(data: String, position: Int) {
            itemBinding.tvRoomNumber.text = data


            itemBinding.root.setOnClickListener {
                listener.onRoomClick(data, position)
            }

            itemBinding.btnAddTenant.setOnClickListener {
                listener.onAddTenantClick(data, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManagePropertyViewHolder {
        val itemBinding = PropertyRoomItemsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ManagePropertyViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ManagePropertyViewHolder, position: Int) {
        val data = roomList[position]
        holder.setData(data, position)
    }

    override fun getItemCount(): Int = roomList.size
}
