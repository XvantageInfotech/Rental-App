package com.xvantage.rental.ui.manageProperty.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xvantage.rental.data.source.Tenant
import com.xvantage.rental.databinding.PropertyRoomItemsBinding
import com.xvantage.rental.utils.AppPreference

class ManagePropertyAdapter(
    private val context: Context,
) : RecyclerView.Adapter<ManagePropertyAdapter.ManagePropertyViewHolder>() {

    private lateinit var appPreference: AppPreference
    private var readImagePermission: String? = null
    private lateinit var roomList: List<String>

    @SuppressLint("NotifyDataSetChanged")
    fun addItems(roomList: List<String>) {
        this.roomList = roomList
        notifyDataSetChanged()
    }


    inner class ManagePropertyViewHolder(private val itemBinding: PropertyRoomItemsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
        fun setData(data: String, position: Int) {
            itemBinding.tvRoomNumber.text = data
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManagePropertyViewHolder {
        val itemBinding = PropertyRoomItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ManagePropertyViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ManagePropertyViewHolder, position: Int) {
        val data = roomList[position]
        appPreference = AppPreference(context)
        holder.setData(data, position)
    }

    override fun getItemCount(): Int = roomList.size
}