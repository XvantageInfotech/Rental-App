package com.xvantage.rental.ui.addProperty.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.xvantage.rental.BuildConfig
import com.xvantage.rental.R
import com.xvantage.rental.databinding.ActivityAddPropertyBinding
import com.xvantage.rental.network.request.property.CreatePropertyRequest
import com.xvantage.rental.ui.addProperty.AddPropertyViewModel
import com.xvantage.rental.ui.addProperty.CreatePropertyState
import com.xvantage.rental.utils.AppPreference
import com.xvantage.rental.utils.CommonFunction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.File

@AndroidEntryPoint
class AddPropertyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPropertyBinding
    private lateinit var appPreference: AppPreference
    private val viewModel: AddPropertyViewModel by viewModels()
    private var propertyTypeIds = listOf<String>()
    private var selectedPropertyTypeId: String = ""
    private lateinit var llPropertyImage: View

    private var currentNumber = 0

    private var propertyImage: Uri? = null

    // private val viewModel: AddPropertyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_property)
        appPreference = AppPreference(this)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding.toolbar.tvTitle.setText(R.string.add_property_bottom_n)


        llPropertyImage = findViewById(R.id.ll_property_photo)

        initViews()
        initClickEvents()
    }

    /**
     * Initialize click event listeners for UI components.
     */
    private fun initClickEvents() {
        binding.toolbar.back.setOnClickListener { onBackPressed() }
        binding.spinnerPropertyType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                selectedPropertyTypeId = if (pos > 0) propertyTypeIds[pos - 1] else ""
                Log.d("AddProperty", "Selected typeId=$selectedPropertyTypeId")
                updatePropertySection(pos)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        binding.toolbar.btnSave.setOnClickListener {
            val request = CreatePropertyRequest(
                address        = binding.etAddress.text.toString().trim(),
                noOfRoom       = binding.etHomeNumber.text.toString().toIntOrNull() ?: 0,
                propertyTypeId = selectedPropertyTypeId,
                wa_number      = binding.etWhatsappNumber.text.toString().trim(),
                name           = binding.etOwnerName.text.toString().trim(),
                propertyImage  = propertyImage?.let { uriToBase64(it) } ?: ""
            )
            viewModel.createProperty(request)
        }
        lifecycleScope.launch {
            viewModel.createPropertyState.collect { state ->
                when (state) {
                    is CreatePropertyState.Loading -> {
                        // TODO: show loading indicator
                    }
                    is CreatePropertyState.Success -> {
                        Toast.makeText(
                            this@AddPropertyActivity,
                            "Property created: ${state.data.data.id}",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                    is CreatePropertyState.Error -> {
                        Toast.makeText(this@AddPropertyActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                    is CreatePropertyState.Idle -> Unit
                }
            }
        }

        binding.llAddPhoto.setOnClickListener { checkPermissionsAndOpenOptions() }

        binding.llPropertyPhoto.btnClose.setOnClickListener {
            propertyImage = null
            llPropertyImage.visibility = View.GONE
            binding.llAddPhoto.visibility = View.VISIBLE
        }
    }

    /**
     * Initialize all views and drop-down menus.
     */
    private fun initViews() {
        viewModel.loadPropertyTypes()
        // 2) Collect and populate spinner
        lifecycleScope.launchWhenStarted {
            viewModel.propertyTypes.collect { list ->
                val names = list.map { it.name }
                propertyTypeIds = list.map { it.id }
                // populate spinner, but do NOT call setSelection here
                setupSpinner(binding.spinnerPropertyType, listOf("Select Property Type") + names)
            }
        }
    }



    /**
     * Helper function to setup a spinner with custom text color.
     */
    private fun setupSpinner(
        spinner: Spinner,
        items: List<String>,
        defaultTextColor: Int = Color.GRAY,
        selectedTextColor: Int = Color.BLACK
    ) {
        val adapter = object : ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items) {
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(if (position == 0) defaultTextColor else selectedTextColor)
                return view
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                (view as? TextView)?.setTextColor(if (position == 0) defaultTextColor else selectedTextColor)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        spinner.setSelection(0)
    }



    /**
     * Setup the Property Type spinner and update UI sections based on selection.
     */
    private fun setupPropertyTypeSpinner() {

        val spinner = findViewById<Spinner>(R.id.spinner_property_type)
        lifecycleScope.launchWhenStarted {
            viewModel.propertyTypes.collect { list ->
                val displayNames = list.map { it.name }
                propertyTypeIds = list.map { it.id }
                setupSpinner(spinner, listOf("Select…") + displayNames)  // reuse your helper
            }
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                (view as? TextView)?.setTextColor(if (position == 0) Color.GRAY else Color.BLACK)
                if (position > 0) {
                    selectedPropertyTypeId = propertyTypeIds[position - 1]
                } else {
                    selectedPropertyTypeId = ""
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        spinner.setSelection(0)
    }
    private fun uriToBase64(uri: Uri): String {
        return contentResolver.openInputStream(uri)!!.use { stream ->
            val bytes = stream.readBytes()
            Base64.encodeToString(bytes, Base64.NO_WRAP)
        }
    }

    /**
     * Update the visibility of UI sections based on the selected property type.
     */
    private fun updatePropertySection(selectedPosition: Int) {
        when (selectedPosition) {
            1 -> { // House
                binding.llHomeNumber.visibility = View.VISIBLE
            }
            2 -> { // Apartment
                binding.llHomeNumber.visibility = View.GONE
            }
            3 -> { // PG
                binding.llHomeNumber.visibility = View.GONE
            }
            4 -> { // Rent House
                binding.llHomeNumber.visibility = View.GONE
            }
            5 -> { // Land
                binding.llHomeNumber.visibility = View.GONE
            }
            else -> { // Default: No section selected
                binding.llHomeNumber.visibility = View.GONE
            }
        }
    }

    /**
     * Checks required permissions and opens photo options dialog.
     */
    private fun checkPermissionsAndOpenOptions() {
        val requiredPermissions = mutableListOf<String>()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requiredPermissions.add(Manifest.permission.CAMERA)
        }
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requiredPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (requiredPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, requiredPermissions.toTypedArray(), PERMISSION_REQUEST_CODE)
        } else {
            showPhotoOptionsDialog()
        }
    }

    /**
     * Display a dialog to choose between camera and gallery.
     */
    private fun showPhotoOptionsDialog() {
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

    // ActivityResultLaunchers for Camera and Gallery
    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                propertyImage?.let { updatePhotoUI(it) }
                    ?: Toast.makeText(this, "Failed to capture photo!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Photo capture cancelled!", Toast.LENGTH_SHORT).show()
            }
        }

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                propertyImage = uri
                updatePhotoUI(uri)
            } ?: Toast.makeText(this, "Failed to upload photo!", Toast.LENGTH_SHORT).show()
        }

    /**
     * Update the UI with the selected or captured photo.
     */
    private fun updatePhotoUI(photoUri: Uri) {
        binding.llPropertyPhoto.ivThumbnail.setImageURI(photoUri)
        val fileName = CommonFunction().getFileName(this, photoUri)
        binding.llPropertyPhoto.tvFileName.text = fileName
        val fileSize = CommonFunction().getFileSize(this, photoUri)
        binding.llPropertyPhoto.tvFileSize.text = fileSize

        propertyImage = photoUri
        llPropertyImage.visibility = View.VISIBLE
        binding.llAddPhoto.visibility = View.GONE
    }

    /**
     * Opens the device camera to capture a photo.
     */
    private fun openCamera() {
        val photoFile = File(
            getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "IMG_${System.currentTimeMillis()}.jpg"
        )
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

    /**
     * Opens the gallery for image selection.
     */
    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                showPhotoOptionsDialog()
            } else {
                Toast.makeText(this, "Permissions are required to proceed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 101
    }
}