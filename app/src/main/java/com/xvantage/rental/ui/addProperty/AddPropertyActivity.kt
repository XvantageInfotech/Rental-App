package com.xvantage.rental.ui.addProperty

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
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
import androidx.core.content.FileProvider
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.xvantage.rental.BuildConfig
import com.xvantage.rental.R
import com.xvantage.rental.databinding.ActivityAddPropertyBinding
import com.xvantage.rental.ui.manageProperty.ManagePropertyActivity
import com.xvantage.rental.utils.AppPreference
import com.xvantage.rental.utils.CommonFunction
import java.io.File

class AddPropertyActivity : AppCompatActivity() {

    private lateinit var layoutBinding: ActivityAddPropertyBinding
    lateinit var appPreference: AppPreference
    private var currentNumber = 0
    private lateinit var llAppartment: View
    private lateinit var llPG: View
    private lateinit var llLand: View
    private lateinit var llRentHouse:View
    private lateinit var llPropertyImage:View
    private var propertyImage:Uri?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_property)
        appPreference = AppPreference(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        layoutBinding.toolbar.tvTitle.setText(R.string.add_property_bottom_n)
        llAppartment = findViewById(R.id.ll_appartment)
        llPG = findViewById(R.id.ll_pg_rooms)
        llLand = findViewById(R.id.ll_land_detail)
        llRentHouse = findViewById(R.id.ll_rent_house)
        llPropertyImage = findViewById(R.id.ll_property_photo)
        initView()
        clickEvents()
    }

    private fun clickEvents() {
        layoutBinding.toolbar.back.setOnClickListener {
            onBackPressed()
        }
        layoutBinding.toolbar.btnSave.setOnClickListener {
            CommonFunction().navigation(this,ManagePropertyActivity::class.java)
        }

        layoutBinding.llAddPhoto.setOnClickListener {
            checkPermissionsAndOpenOptions()
        }
        layoutBinding.llPropertyPhoto.btnClose.setOnClickListener {
            propertyImage=null
            llPropertyImage.visibility=View.GONE
            layoutBinding.llAddPhoto.visibility=View.VISIBLE
        }
    }

    private fun initView() {
        propertyTypeDropDown()
        appartmentLayout()
        PgRoomsLayout()
        LandDetailLayout()
        RentHouseLayout()
    }

    private fun RentHouseLayout(){
        RoomTypeDropDown()
    }

    private fun LandDetailLayout(){
        var landSize=layoutBinding.llLandDetail.etLandSize.text.toString()
        LandAreaTypeDropDown()
    }
    private fun PgRoomsLayout(){

        layoutBinding.llPgRooms.incRoom.setOnClickListener {
            if (currentNumber < 99) {
                currentNumber++
                layoutBinding.llPgRooms.etRoomNumber.setText(String.format("%02d", currentNumber))
            }
        }

        layoutBinding.llPgRooms.decRoom.setOnClickListener {
            if (currentNumber > 0) {
                currentNumber--
                layoutBinding.llPgRooms.etRoomNumber.setText(String.format("%02d", currentNumber))
            }
        }
        layoutBinding.llPgRooms.incBeds.setOnClickListener {
            if (currentNumber < 99) {
                currentNumber++
                layoutBinding.llPgRooms.etBedNumber.setText(String.format("%02d", currentNumber))
            }
        }

        layoutBinding.llPgRooms.decBeds.setOnClickListener {
            if (currentNumber > 0) {
                currentNumber--
                layoutBinding.llPgRooms.etBedNumber.setText(String.format("%02d", currentNumber))
            }
        }

    }

    private fun appartmentLayout() {
        layoutBinding.llAppartment.buttonUp.setOnClickListener {
            if (currentNumber < 99) {
                currentNumber++
                layoutBinding.llAppartment.etNumber.setText(String.format("%02d", currentNumber))
            }
        }

        layoutBinding.llAppartment.buttonDown.setOnClickListener {
            if (currentNumber > 0) {
                currentNumber--
                layoutBinding.llAppartment.etNumber.setText(String.format("%02d", currentNumber))
            }
        }

        flatTypeDropDown()
    }

    private fun RoomTypeDropDown() {
        val spinner = findViewById<Spinner>(R.id.spinner_room_type)
        val propertyTypes = listOf("Select Room Type","BK", "1BHK", "2BHK", "3BHK", "4BHK")
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
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        spinner.setSelection(0)
    }

    private fun flatTypeDropDown() {
        val spinner = findViewById<Spinner>(R.id.spinner_flat_type)
        val propertyTypes = listOf("Select Flat Type", "1BHK", "2BHK", "3BHK", "4BHK")
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
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        spinner.setSelection(0)
    }

    private fun LandAreaTypeDropDown() {
        val spinner = findViewById<Spinner>(R.id.spinner_area_type)
        val propertyTypes = listOf("Sqft", "Sqmt")
        val adapter = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            propertyTypes
        ) {
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                return view
            }
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        spinner.setSelection(0)
    }

    private fun propertyTypeDropDown() {
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
                if (position==1){      //House Section
                    layoutBinding.llHomeNumber.visibility=View.VISIBLE
                    llAppartment.visibility = View.GONE
                    llPG.visibility = View.GONE
                    llLand.visibility = View.GONE
                    llRentHouse.visibility = View.GONE

                }
                else if (position==2){      //Apartment Section
                    llAppartment.visibility = View.VISIBLE
                    layoutBinding.llHomeNumber.visibility=View.GONE
                    llPG.visibility = View.GONE
                    llLand.visibility = View.GONE
                    llRentHouse.visibility = View.GONE
                }
                else if (position==3){  //PG Section
                    llPG.visibility = View.VISIBLE
                    layoutBinding.llHomeNumber.visibility=View.GONE
                    llAppartment.visibility = View.GONE
                    llLand.visibility = View.GONE
                    llRentHouse.visibility = View.GONE
                }
                else if (position==4){  //Rent House Section
                    llRentHouse.visibility = View.VISIBLE
                    layoutBinding.llHomeNumber.visibility=View.GONE
                    llAppartment.visibility = View.GONE
                    llLand.visibility = View.GONE
                    llPG.visibility = View.GONE
                }
                else if(position==5){   //Land Section
                    llLand.visibility = View.VISIBLE
                    layoutBinding.llHomeNumber.visibility=View.GONE
                    llAppartment.visibility = View.GONE
                    llPG.visibility = View.GONE
                    llRentHouse.visibility = View.GONE
                }
                else{    //Select Property Type Section
                    layoutBinding.llHomeNumber.visibility=View.GONE
                    llAppartment.visibility = View.GONE
                    llPG.visibility = View.GONE
                    llLand.visibility = View.GONE
                    llRentHouse.visibility = View.GONE
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
            propertyImage?.let {
                updateUi(it)
            } ?: run {
                Toast.makeText(this, "Failed to capture photo!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Photo capture cancelled!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun updateUi(photoUri:Uri) {
        layoutBinding.llPropertyPhoto.ivThumbnail.setImageURI(photoUri)
        val fileName = CommonFunction().getFileName(this,photoUri)
        layoutBinding.llPropertyPhoto.tvFileName.text = fileName
        val fileSize =CommonFunction(). getFileSize(this,photoUri)
        layoutBinding.llPropertyPhoto.tvFileSize.text = fileSize
        propertyImage = photoUri
        llPropertyImage.visibility=View.VISIBLE
        layoutBinding.llAddPhoto.visibility=View.GONE
    }


    private fun openCamera() {
        val photoFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "IMG_${System.currentTimeMillis()}.jpg")

        propertyImage = FileProvider.getUriForFile(
            this,
            "${BuildConfig.APPLICATION_ID}.fileprovider",
            photoFile
        )

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, propertyImage)
        }

        if (intent.resolveActivity(packageManager) != null) {
            cameraLauncher.launch(intent)
        } else {
            Toast.makeText(this, "No camera app available", Toast.LENGTH_SHORT).show()
        }
    }


    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            propertyImage=uri
            if (uri != null) {
                updateUi(uri)
            } else {
                Toast.makeText(this, "Failed to upload photo!", Toast.LENGTH_SHORT).show()
            }
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
