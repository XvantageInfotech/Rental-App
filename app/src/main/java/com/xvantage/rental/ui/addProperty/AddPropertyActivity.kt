package com.xvantage.rental.ui.addProperty

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.xvantage.rental.R
import com.xvantage.rental.databinding.ActivityAddPropertyBinding
import com.xvantage.rental.utils.AppPreference

class AddPropertyActivity : AppCompatActivity() {

    private lateinit var layoutBinding: ActivityAddPropertyBinding
    lateinit var appPreference: AppPreference
    private var currentNumber = 0
    private lateinit var llAppartment: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_property)
        appPreference = AppPreference(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        layoutBinding.toolbar.tvTitle.setText(R.string.add_property_bottom_n)
        llAppartment = findViewById(R.id.ll_appartment)
        initView()
        clickEvents()
    }

    private fun clickEvents() {
        layoutBinding.toolbar.back.setOnClickListener {
            onBackPressed()
        }

        layoutBinding.llAddPhoto.setOnClickListener {
            checkPermissionsAndOpenOptions()
        }
        
    }

    private fun initView() {
        dropDownSpinner()
        appartmentLayout()
    }

    private fun appartmentLayout() {
        layoutBinding.llAppartment.buttonUp.setOnClickListener {
            if (currentNumber < 99) { // Set max limit if needed
                currentNumber++
                layoutBinding.llAppartment.etNumber.setText(String.format("%02d", currentNumber))
            }
        }

        // Decrement on down button click
        layoutBinding.llAppartment.buttonDown.setOnClickListener {
            if (currentNumber > 0) { // Set min limit if needed
                currentNumber--
                layoutBinding.llAppartment.etNumber.setText(String.format("%02d", currentNumber))
            }
        }
    }

    private fun dropDownSpinner() {
        val spinner = findViewById<Spinner>(R.id.spinner_property_type)
        val propertyTypes = listOf("Select Property Type", "House", "Apartment", "PG", "Rent House", "Land")
        val adapter = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            propertyTypes
        ) {
            
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(if (position == 0) Color.GRAY else Color.BLACK)
                return view
            }
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                (view as? TextView)?.setTextColor(if (position == 0) Color.GRAY else Color.BLACK)
                if (position==0){
                    layoutBinding.llHomeNumber.visibility=View.GONE
                    llAppartment.visibility = View.GONE
                }
                else if (position==1){
                    layoutBinding.llHomeNumber.visibility=View.VISIBLE
                    llAppartment.visibility = View.GONE
                }
                else if (position==2){
                    layoutBinding.llHomeNumber.visibility=View.GONE
                    llAppartment.visibility = View.VISIBLE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        spinner.setSelection(0)
    }

    private fun checkPermissionsAndOpenOptions() {
        val permissions = mutableListOf<String>()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CAMERA)
        }
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), PERMISSION_REQUEST_CODE)
        } else {
            showOptionsDialog()
        }
    }

    private fun showOptionsDialog() {
        val options = arrayOf("Open Camera", "Choose from Gallery")
        MaterialAlertDialogBuilder(this)
            .setTitle("Add Photo")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> openGallery()
                }
            }
            .show()
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val photoUri = result.data?.data
            // Handle the captured photo
            Toast.makeText(this, "Photo captured successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            // Handle the selected image
            Toast.makeText(this, "Photo selected: $uri", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                showOptionsDialog()
            } else {
                Toast.makeText(this, "Permissions are required to proceed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 101
    }
}
