package com.xvantage.rental.ui.dashboard.fragment.adapter

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager.Properties
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.xvantage.rental.databinding.HomePropertiesItemBinding
import com.xvantage.rental.utils.AppPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Locale

interface OnPropertyActionListener {
    fun onDeleteClicked(billInvoice: Properties )
    fun onMoreClicked(billInvoice: Properties)
}

class PropertiesAdapter(
    private val context: Context,
    private val propertyActionListener: OnPropertyActionListener
) : RecyclerView.Adapter<PropertiesAdapter.PropertyDetailsViewHolder>() {

    private lateinit var appPreference: AppPreference
    private var readImagePermission: String? = null
    private var billInvoiceList: ArrayList<Properties> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun addItems(billInvoiceList: ArrayList<Properties>) {
        this.billInvoiceList = billInvoiceList
        notifyDataSetChanged()
    }


    inner class PropertyDetailsViewHolder(private val itemBinding: HomePropertiesItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
        fun setData(data: Properties, position: Int) {


        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyDetailsViewHolder {
        val itemBinding = HomePropertiesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PropertyDetailsViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: PropertyDetailsViewHolder, position: Int) {
        val data = billInvoiceList[position]
        appPreference = AppPreference(context)
        holder.setData(data, position)
    }

    override fun getItemCount(): Int = billInvoiceList.size
}
