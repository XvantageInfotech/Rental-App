package com.xvantage.rental.ui.addTenant

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.xvantage.rental.BuildConfig
import com.xvantage.rental.R
import com.xvantage.rental.databinding.ActivityAddTenantBinding
import com.xvantage.rental.utils.AppPreference
import com.xvantage.rental.utils.CommonFunction
import java.io.File
import java.util.Calendar

class AddTenantActivity : AppCompatActivity() {

    private lateinit var layoutBinding: ActivityAddTenantBinding
    lateinit var appPreference: AppPreference
    private var frontAdharImageUri: Uri?=null
    private var backAdharImageUri: Uri?=null
    private var tenantImageUri: Uri?=null
    private lateinit var llTenantPhoto:View
    private lateinit var llFrontAdharPhoto:View
    private lateinit var llBackAdharPhoto:View
    private val PERMISSION_REQUEST_CODE = 101
    private var selectedPicker=1
    private var tenantDetailExpanded = true
    private var rentDetailExpanded = false
    private var waterBillDetailExpanded = false
    val spinnerElecAndWaterOptions = arrayOf("No cost", "Fixed", "Metered")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_tenant)
        appPreference = AppPreference(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        layoutBinding.toolbar.tvTitle.setText(R.string.add_tenant_property)
        llTenantPhoto = findViewById(R.id.ll_selected_tenant_photo)
        llFrontAdharPhoto = findViewById(R.id.ll_selected_front_adhar_photo)
        llBackAdharPhoto = findViewById(R.id.ll_selected_back_adhar_photo)

       onClickEvents()


    }

    private fun onClickEvents() {
        layoutBinding.llAddTenantPhoto.setOnClickListener {
            selectedPicker=1
            checkPermissionsAndOpenOptions()
        }
        layoutBinding.llAddFrontAdhar.setOnClickListener {
            selectedPicker=2
            checkPermissionsAndOpenOptions()
        }
        layoutBinding.llAddBackAdhar.setOnClickListener {
            selectedPicker=3
            checkPermissionsAndOpenOptions()
        }
        layoutBinding.toolbar.back.setOnClickListener {
            onBackPressed()
        }
        layoutBinding.llSelectedTenantPhoto.btnClose.setOnClickListener {
            tenantImageUri=null
            llTenantPhoto.visibility=View.GONE
            layoutBinding.llAddTenantPhoto.visibility=View.VISIBLE
        }
        layoutBinding.llSelectedFrontAdharPhoto.btnClose.setOnClickListener {
            frontAdharImageUri=null
            llFrontAdharPhoto.visibility=View.GONE
            layoutBinding.llAddFrontAdhar.visibility=View.VISIBLE
        }
        layoutBinding.llSelectedBackAdharPhoto.btnClose.setOnClickListener {
            backAdharImageUri=null
            llBackAdharPhoto.visibility=View.GONE
            layoutBinding.llAddBackAdhar.visibility=View.VISIBLE
        }
        layoutBinding.llRentFinanceDetail.tvMoveInDate.setOnClickListener {
            CommonFunction().showDatePickerDialog(
                context = this,
                onDateSelected = { selectedDate ->
                    layoutBinding.llRentFinanceDetail.tvMoveInDate.text = selectedDate
                }
            )
        }
        layoutBinding.llRentFinanceDetail.tvRentStartDate.setOnClickListener {
            CommonFunction().showDatePickerDialog(
                context = this,
                onDateSelected = { selectedDate ->
                    layoutBinding.llRentFinanceDetail.tvRentStartDate.text = selectedDate
                }
            )
        }
        layoutBinding.llRentFinanceDetail.tvRentDueDate.setOnClickListener {
            CommonFunction().showDatePickerDialog(
                context = this,
                onDateSelected = { selectedDate ->
                    layoutBinding.llRentFinanceDetail.tvRentDueDate.text = selectedDate
                }
            )
        }

        layoutBinding.llRentFinanceDetail.tvAggreementStartDate.setOnClickListener {
            CommonFunction().showDatePickerDialog(
                context = this,
                onDateSelected = { selectedDate ->
                    layoutBinding.llRentFinanceDetail.tvAggreementStartDate.text = selectedDate
                }
            )
        }
        layoutBinding.llRentFinanceDetail.tvAggreementEndDate.setOnClickListener {
            CommonFunction().showDatePickerDialog(
                context = this,
                onDateSelected = { selectedDate ->
                    layoutBinding.llRentFinanceDetail.tvAggreementEndDate.text = selectedDate
                }
            )
        }

        layoutBinding.llRentFinanceDetail.rgLeaseType.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_until_leave -> {
                    layoutBinding.llRentFinanceDetail.llLeaseDateSelection.visibility=View.GONE
                    Toast.makeText(this, "Until Leave selected", Toast.LENGTH_SHORT).show()
                }
                R.id.rb_fixed_define -> {
                    // Handle "Fixed Define" option selected
                    layoutBinding.llRentFinanceDetail.llLeaseDateSelection.visibility=View.VISIBLE
                }
            }
        }

        layoutBinding.tvTitleTenantDetail.setOnClickListener {
            if (tenantDetailExpanded) {
                layoutBinding.llTenantDetail.visibility = View.GONE
                layoutBinding.tvTitleTenantDetail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_drop_down_arrow, 0)
            } else {
                layoutBinding.llTenantDetail.visibility = View.VISIBLE
                layoutBinding.tvTitleTenantDetail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_up_arrow, 0)
            }
            tenantDetailExpanded = !tenantDetailExpanded
        }

        layoutBinding.tvTitleRentDetail.setOnClickListener {
            if (rentDetailExpanded) {
                layoutBinding.llRentDetail.visibility = View.GONE
                layoutBinding.tvTitleRentDetail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_drop_down_arrow, 0)
            } else {
                layoutBinding.llRentDetail.visibility = View.VISIBLE
                layoutBinding.tvTitleRentDetail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_up_arrow, 0)
            }
            rentDetailExpanded = !rentDetailExpanded
        }
        layoutBinding.tvTitleElectWaterDetail.setOnClickListener {
            if (waterBillDetailExpanded) {
                layoutBinding.llEleWaterDetail.visibility = View.GONE
                layoutBinding.tvTitleElectWaterDetail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_drop_down_arrow, 0)
            } else {
                layoutBinding.llEleWaterDetail.visibility = View.VISIBLE
                layoutBinding.tvTitleElectWaterDetail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_up_arrow, 0)
            }
            waterBillDetailExpanded = !waterBillDetailExpanded
        }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            spinnerElecAndWaterOptions
        )
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
        layoutBinding.llElectricityFinanceDetail.spElectricity.adapter = adapter
        layoutBinding.llWaterFinanceDetail.spWater.adapter = adapter

        layoutBinding.llElectricityFinanceDetail.spElectricity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parentView?.getItemAtPosition(position) as String

                when(position){
                    0->{
                        layoutBinding.llElectricityFinanceDetail.llDefaultElectricityDetails.visibility=View.GONE
                        layoutBinding.llElectricityFinanceDetail.llElectricityMeterDetails.visibility=View.GONE
                    }
                    1->{
                        layoutBinding.llElectricityFinanceDetail.llDefaultElectricityDetails.visibility=View.VISIBLE
                        layoutBinding.llElectricityFinanceDetail.llElectricityMeterDetails.visibility=View.GONE
                    }
                    2->{
                        layoutBinding.llElectricityFinanceDetail.llElectricityMeterDetails.visibility=View.VISIBLE
                        layoutBinding.llElectricityFinanceDetail.llDefaultElectricityDetails.visibility=View.GONE
                    }
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        }

        layoutBinding.llWaterFinanceDetail.spWater.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parentView?.getItemAtPosition(position) as String

                when(position){
                    0->{
                        layoutBinding.llWaterFinanceDetail.llDefaultWaterDetails.visibility=View.GONE
                        layoutBinding.llWaterFinanceDetail.llWaterMeterDetails.visibility=View.GONE
                    }
                    1->{
                        layoutBinding.llWaterFinanceDetail.llDefaultWaterDetails.visibility=View.VISIBLE
                        layoutBinding.llWaterFinanceDetail.llWaterMeterDetails.visibility=View.GONE
                    }
                    2->{
                        layoutBinding.llWaterFinanceDetail.llWaterMeterDetails.visibility=View.VISIBLE
                        layoutBinding.llWaterFinanceDetail.llDefaultWaterDetails.visibility=View.GONE
                    }
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
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

    private fun openCamera() {
        val photoFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "IMG_${System.currentTimeMillis()}.jpg")
        var intent:Intent?=null
        when(selectedPicker){
            1->{
                tenantImageUri= FileProvider.getUriForFile(
                    this,
                    "${BuildConfig.APPLICATION_ID}.fileprovider",
                    photoFile
                )
                intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                    putExtra(MediaStore.EXTRA_OUTPUT, tenantImageUri)
                }
            }
            2->{
                frontAdharImageUri= FileProvider.getUriForFile(
                    this,
                    "${BuildConfig.APPLICATION_ID}.fileprovider",
                    photoFile
                )
                intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                    putExtra(MediaStore.EXTRA_OUTPUT, frontAdharImageUri)
                }
            }
            3->{
                backAdharImageUri= FileProvider.getUriForFile(
                    this,
                    "${BuildConfig.APPLICATION_ID}.fileprovider",
                    photoFile
                )
                intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                    putExtra(MediaStore.EXTRA_OUTPUT, backAdharImageUri)
                }
            }
        }


        if (intent?.resolveActivity(packageManager) != null) {
            cameraLauncher.launch(intent)
        } else {
            Toast.makeText(this, "No camera app available", Toast.LENGTH_SHORT).show()
        }
    }


    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            when(selectedPicker){
                1->{
                    tenantImageUri=uri
                }
                2->{
                    frontAdharImageUri=uri
                }
                3->{
                    backAdharImageUri=uri
                }
            }

            if (uri != null) {
                updateUi(uri)
            } else {
                Toast.makeText(this, "Failed to upload photo!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {

           when(selectedPicker){
               1->{
                   tenantImageUri?.let {
                        updateUi(it)
                   } ?: run {
                       Toast.makeText(this, "Failed to capture photo!", Toast.LENGTH_SHORT).show()
                   }
               }
               2->{
                   frontAdharImageUri?.let {
                        updateUi(it)
                   } ?: run {
                       Toast.makeText(this, "Failed to capture photo!", Toast.LENGTH_SHORT).show()
                   }
               }
               3->{
                   backAdharImageUri?.let {
                        updateUi(it)
                   } ?: run {
                       Toast.makeText(this, "Failed to capture photo!", Toast.LENGTH_SHORT).show()
                   }
               }
           }

        } else {
            Toast.makeText(this, "Photo capture cancelled!", Toast.LENGTH_SHORT).show()
        }
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

    private fun openGallery() {
        galleryLauncher.launch("image/*")
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
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        } else {
            showOptionsDialog()
        }
    }

    private fun updateUi(photoUri: Uri) {

        when (selectedPicker) {
            1 -> {      //Tennant photo
                layoutBinding.llSelectedTenantPhoto.ivThumbnail.setImageURI(photoUri)
                val fileName = CommonFunction().getFileName(this, photoUri)
                layoutBinding.llSelectedTenantPhoto.tvFileName.text = fileName
                val fileSize = CommonFunction().getFileSize(this, photoUri)
                layoutBinding.llSelectedTenantPhoto.tvFileSize.text = fileSize
                tenantImageUri = photoUri
                llTenantPhoto.visibility = View.VISIBLE
                layoutBinding.llAddTenantPhoto.visibility = View.GONE
            }
            2 -> {  //Front Adhar photo
                layoutBinding.llSelectedFrontAdharPhoto.ivThumbnail.setImageURI(photoUri)
                val fileName = CommonFunction().getFileName(this, photoUri)
                layoutBinding.llSelectedFrontAdharPhoto.tvFileName.text = fileName
                val fileSize = CommonFunction().getFileSize(this, photoUri)
                layoutBinding.llSelectedFrontAdharPhoto.tvFileSize.text = fileSize
                frontAdharImageUri = photoUri
                llFrontAdharPhoto.visibility = View.VISIBLE
                layoutBinding.llAddFrontAdhar.visibility = View.GONE
            }
            3 -> {      //Back Adhar photo
                layoutBinding.llSelectedBackAdharPhoto.ivThumbnail.setImageURI(photoUri)
                val fileName = CommonFunction().getFileName(this, photoUri)
                layoutBinding.llSelectedBackAdharPhoto.tvFileName.text = fileName
                val fileSize = CommonFunction().getFileSize(this, photoUri)
                layoutBinding.llSelectedBackAdharPhoto.tvFileSize.text = fileSize
                backAdharImageUri = photoUri
                llBackAdharPhoto.visibility = View.VISIBLE
                layoutBinding.llAddBackAdhar.visibility = View.GONE
            }

        }
    }
}