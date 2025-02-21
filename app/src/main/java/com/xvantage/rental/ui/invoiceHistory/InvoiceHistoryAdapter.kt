package com.xvantage.rental.ui.invoiceHistory

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xvantage.rental.databinding.PaymentHistoryCardBinding
import com.xvantage.rental.ui.rentInvoice.RentInvoiceActivity

class InvoiceHistoryAdapter(private val invoiceList: List<InvoiceHistoryActivity.InvoiceHistoryItem>,val context: Context) : RecyclerView.Adapter<InvoiceHistoryAdapter.InvoiceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvoiceViewHolder {
        val binding = PaymentHistoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InvoiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InvoiceViewHolder, position: Int) {
        val invoice = invoiceList[position]
        holder.binding.roomImage.setImageResource(invoice.roomImageResId)
        holder.binding.roomId.text = invoice.roomId+" "+invoice.tenantName
        holder.binding.tvAddress.text = invoice.address
        holder.binding.tvPayment.text = invoice.paymentAmount
        holder.binding.tvPaymentDate.text = invoice.paymentDate
        holder.binding.tvNote.text = invoice.note
        holder.binding.paymentHistoryCard.setOnClickListener {
            context.startActivity(Intent(context, RentInvoiceActivity::class.java))
        }

    }

    override fun getItemCount(): Int = invoiceList.size

    class InvoiceViewHolder(val binding: PaymentHistoryCardBinding) : RecyclerView.ViewHolder(binding.root)
}
