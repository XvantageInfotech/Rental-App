package com.xvantage.rental.utils

import android.app.ProgressDialog
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    private lateinit var progressDialog: ProgressDialog

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initProgressDialog(context)
    }

    private fun initProgressDialog(context: Context) {
        progressDialog = ProgressDialog(context).apply {
            setMessage("Loading...")
            setCancelable(false)
        }
    }

    fun showProgressBar() {
        if (!progressDialog.isShowing) progressDialog.show()
    }

    fun hideProgressBar() {
        if (progressDialog.isShowing) progressDialog.dismiss()
    }

    fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
