package com.xvantage.rental.ui.invoiceHistory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.xvantage.rental.R
import com.xvantage.rental.databinding.ActivityInvoiceHistoryBinding
import com.xvantage.rental.utils.AppPreference

class InvoiceHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInvoiceHistoryBinding
    private lateinit var appPreference: AppPreference
    private lateinit var invoiceAdapter: InvoiceHistoryAdapter
    private val invoiceList = mutableListOf<InvoiceHistoryItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInvoiceHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appPreference = AppPreference(this)

        binding.toolbar.tvTitle.setText(R.string.invoice_history)
        binding.toolbar.back.setOnClickListener {
            onBackPressed()
        }

        binding.rvInvoiceList.layoutManager = LinearLayoutManager(this)
        invoiceAdapter = InvoiceHistoryAdapter(invoiceList,this)
        binding.rvInvoiceList.adapter = invoiceAdapter

        loadInvoiceHistory()
    }

    private fun loadInvoiceHistory() {
        invoiceList.addAll(
            listOf(
                InvoiceHistoryItem(R.drawable.permisson_img, "Room 101","Vipul", "Swastik Plaza", "YogiChowk Surat", "$10000", "1 Jan, 2025","Including All Bills"),
                InvoiceHistoryItem(R.drawable.permisson_img, "Room 101","Vipul", "Swastik Plaza", "YogiChowk Surat", "$12000", "12 feb, 2025","Including All Bills"),
                InvoiceHistoryItem(R.drawable.permisson_img, "Room 101","Vipul", "Swastik Plaza", "YogiChowk Surat", "$22000", "18 Mar, 2025","Including All Bills"),
                InvoiceHistoryItem(R.drawable.permisson_img, "Room 101","Vipul", "Swastik Plaza", "YogiChowk Surat", "$33000", "10 Apr, 2025","Including All Bills"),
                InvoiceHistoryItem(R.drawable.permisson_img, "Room 101","Vipul", "Swastik Plaza", "YogiChowk Surat", "$16000", "11 May, 2025","Including All Bills"),
                InvoiceHistoryItem(R.drawable.permisson_img, "Room 101", "Vipul","Swastik Plaza", "YogiChowk Surat", "$5000", "31 June, 2025","Including All Bills"),

            )
        )
        invoiceAdapter.notifyDataSetChanged()
    }

    data class InvoiceHistoryItem(
        val roomImageResId: Int,
        val roomId: String,
        val tenantName: String,
        val propertyName: String,
        val address: String,
        val paymentAmount: String,
        val paymentDate: String,
        val note:String
    )
}
