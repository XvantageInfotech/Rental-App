package com.xvantage.rental.ui.dashboard.fragment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xvantage.rental.data.source.sample.Property
import com.xvantage.rental.data.source.sample.Tenant
import com.xvantage.rental.databinding.HomeTenantsItemBinding
import com.xvantage.rental.utils.AppPreference


interface OnTenantsActionListener {
    fun onDeleteClicked(billInvoice: Property)
    fun onMoreClicked(billInvoice: Property)
}

class TenantsAdapter(
    private val context: Context,
) : RecyclerView.Adapter<TenantsAdapter.TenantDetailsViewHolder>() {

    private lateinit var appPreference: AppPreference
    private var readImagePermission: String? = null
    private lateinit var billInvoiceList: List<Tenant>

    @SuppressLint("NotifyDataSetChanged")
    fun addItems(billInvoiceList: List<Tenant>) {
        this.billInvoiceList = billInvoiceList
        notifyDataSetChanged()
    }


    inner class TenantDetailsViewHolder(private val itemBinding: HomeTenantsItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
        fun setData(data: Tenant, position: Int) {
            itemBinding.tvTenantName.text = data.tenantName
            itemBinding.tvLocation.text = data.tenantEmail
            itemBinding.tvNumber.text = data.contactInfo
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TenantDetailsViewHolder {
        val itemBinding = HomeTenantsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TenantDetailsViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: TenantDetailsViewHolder, position: Int) {
        val data = billInvoiceList[position]
        appPreference = AppPreference(context)
        holder.setData(data, position)
    }

    override fun getItemCount(): Int = billInvoiceList.size
}
