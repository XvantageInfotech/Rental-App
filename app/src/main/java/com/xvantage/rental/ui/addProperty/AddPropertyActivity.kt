package com.xvantage.rental.ui.addProperty

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
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
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.xvantage.rental.BuildConfig
import com.xvantage.rental.R
import com.xvantage.rental.databinding.ActivityAddPropertyBinding
import com.xvantage.rental.ui.manageProperty.ManagePropertyActivity
import com.xvantage.rental.utils.AppPreference
import com.xvantage.rental.utils.CommonFunction
import java.io.File


class AddPropertyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPropertyBinding
    private lateinit var appPreference: AppPreference

    private lateinit var llApartment: View
    private lateinit var llPG: View
    private lateinit var llLand: View
    private lateinit var llRentHouse: View
    private lateinit var llPropertyImage: View
    private lateinit var llRentalAgreement: View

    private var currentNumber = 0

    private var propertyImage: Uri? = null
    private var agreementDocument: Uri? = null

    // Define enum for agreement types
    enum class AgreementType {
        NONE,
        DEFAULT,
        UPLOADED,
        CUSTOM
    }

    private var agreementType: AgreementType = AgreementType.NONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_property)
        appPreference = AppPreference(this)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding.toolbar.tvTitle.setText(R.string.add_property_bottom_n)

        llApartment = findViewById(R.id.ll_appartment)
        llPG = findViewById(R.id.ll_pg_rooms)
        llLand = findViewById(R.id.ll_land_detail)
        llRentHouse = findViewById(R.id.ll_rent_house)
        llPropertyImage = findViewById(R.id.ll_property_photo)
        llRentalAgreement = findViewById(R.id.ll_rental_agreement_container)

        initViews()
        initClickEvents()
    }

    /**
     * Initialize click event listeners for UI components.
     */
    private fun initClickEvents() {
        binding.toolbar.back.setOnClickListener { onBackPressed() }

        binding.toolbar.btnSave.setOnClickListener {
            if (validateInputs()) {
                CommonFunction().navigation(this, ManagePropertyActivity::class.java)
            }
        }

        binding.llAddPhoto.setOnClickListener { checkPermissionsAndOpenOptions() }

        binding.llPropertyPhoto.btnClose.setOnClickListener {
            propertyImage = null
            llPropertyImage.visibility = View.GONE
            binding.llAddPhoto.visibility = View.VISIBLE
        }

        // Agreement upload button click
        binding.btnUploadAgreement.setOnClickListener {
            openDocumentPicker()
        }

        // Agreement create button click
        binding.btnCreateAgreement.setOnClickListener {
            // Navigate to create agreement screen or show dialog to create
            showCreateAgreementDialog()
        }

        // Add the default agreement button click
        binding.btnDefaultAgreement.setOnClickListener {
            useDefaultAgreement()
        }

        // Close uploaded agreement document
        // Note: We need to add this to the layout
        if (::binding.isInitialized && binding.llAgreementDocument != null) {
            binding.llAgreementDocument.btnClose.setOnClickListener {
                agreementDocument = null
                binding.llAgreementDocument.root.visibility = View.GONE
                binding.agreementUploadContainer.visibility = View.VISIBLE
                agreementType = AgreementType.NONE
            }
        }

        // Change agreement button click
        // Note: We need to add this to the layout
        if (::binding.isInitialized && binding.agreementStatusLayout != null) {
            binding.agreementStatusLayout.btnChangeAgreement.setOnClickListener {
                showAgreementOptions()
                binding.agreementStatusLayout.llStatusAgreement.visibility = View.GONE
                agreementType = AgreementType.NONE
            }
        }
    }

    /**
     * Validate the input fields.
     */
    private fun validateInputs(): Boolean {
        // Property name and address are optional now, so no validation needed for them

        // Validate property type is selected
        val propertyTypePosition = binding.spinnerPropertyType.selectedItemPosition
        if (propertyTypePosition == 0) {
            Toast.makeText(this, "Please select a property type", Toast.LENGTH_SHORT).show()
            return false
        }

        // Validate that an agreement option is selected
        if (agreementType == AgreementType.NONE) {
            Toast.makeText(this, "Please select a rental agreement option", Toast.LENGTH_SHORT).show()
            return false
        }

        // Additional validations based on property type can be added here

        return true
    }

    /**
     * Initialize all views and drop-down menus.
     */
    private fun initViews() {
        setupPropertyTypeSpinner()
        setupApartmentLayout()
        setupPgRoomsLayout()
        setupLandDetailLayout()
        setupRentHouseLayout()
        setupAgreementSection()
    }

    /**
     * Initialize the rental agreement section.
     */
    private fun setupAgreementSection() {
        // Add the default agreement button to the layout
        // This should be added in the XML file but we're adding the view programmatically for now

        if (!::binding.isInitialized) return

        // Initially hide the document preview
        if (binding.llAgreementDocument != null) {
            binding.llAgreementDocument.root.visibility = View.GONE
        }

        // Initially hide the agreement status layout
        if (binding.agreementStatusLayout != null) {
            binding.agreementStatusLayout.llStatusAgreement.visibility = View.GONE
        }

        // Add default agreement button if it doesn't exist
        if (binding.btnDefaultAgreement == null) {
            val defaultButton = MaterialButton(this).apply {
                id = View.generateViewId()
                text = "Use Default"
                setBackgroundColor(ContextCompat.getColor(context, R.color.light_blue))
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    resources.getDimensionPixelSize(R.dimen.space8),
                    1f
                ).apply {
                    marginStart = resources.getDimensionPixelSize(R.dimen.space8)
                }
                setTextColor(Color.WHITE)
                isAllCaps = false
            }
            defaultButton.setOnClickListener { useDefaultAgreement() }

            // Find the container and add button
            val buttonContainer = findViewById<LinearLayout>(R.id.ll_rental_agreement_buttons)
                ?: findViewById<LinearLayout>(R.id.ll_rental_agreement_container)

            if (buttonContainer != null) {
                buttonContainer.addView(defaultButton)
            }
        }
    }

    /**
     * Shows agreement options (upload, create, default)
     */
    private fun showAgreementOptions() {
        if (!::binding.isInitialized) return

        binding.agreementUploadContainer?.visibility = View.VISIBLE
        binding.btnUploadAgreement?.visibility = View.VISIBLE
        binding.btnCreateAgreement?.visibility = View.VISIBLE
        binding.btnDefaultAgreement?.visibility = View.VISIBLE

        // Make sure document and status views are hidden
        binding.llAgreementDocument?.root?.visibility = View.GONE
        binding.agreementStatusLayout.llStatusAgreement.visibility = View.GONE
    }

    /**
     * Hide agreement options
     */
    private fun hideAgreementOptions() {
        if (!::binding.isInitialized) return

        binding.agreementUploadContainer?.visibility = View.GONE
    }

    /**
     * Set up default agreement
     */
    private fun useDefaultAgreement() {
        hideAgreementOptions()

        agreementType = AgreementType.DEFAULT

        // Create and show agreement status layout if it doesn't exist
        showAgreementStatus("Default Agreement Selected",
            "Standard rental agreement will be used",
            R.drawable.export)
    }

    /**
     * Show the agreement status with custom content
     */
    private fun showAgreementStatus(title: String, description: String, iconResId: Int) {
        // We need to add this layout to the XML file
        // For now, we'll create it dynamically if needed

        // If we have the status layout in the binding
        if (::binding.isInitialized && binding.agreementStatusLayout != null) {
            binding.agreementStatusLayout.tvAgreementStatusTitle.text = title
            binding.agreementStatusLayout.tvAgreementStatusDesc.text = description
            binding.agreementStatusLayout.ivAgreementStatusIcon.setImageResource(iconResId)
            binding.agreementStatusLayout.llStatusAgreement.visibility = View.VISIBLE
            return
        }

        // Otherwise, create it dynamically
        val statusLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            background = ContextCompat.getDrawable(context, R.drawable.grey_border_background)
            setPadding(16, 16, 16, 16)
        }

        val iconView = ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(48, 48)
            setImageResource(iconResId)
        }

        val textContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            ).apply {
                marginStart = 16
            }
        }

        val titleView = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            text = title
            setTextColor(ContextCompat.getColor(context, R.color.text_dark_blue))
            textSize = 16f
            setTypeface(typeface, Typeface.BOLD)
        }

        val descView = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            text = description
            setTextColor(ContextCompat.getColor(context, R.color.grey_hint))
            textSize = 14f
        }

        textContainer.addView(titleView)
        textContainer.addView(descView)

        val changeButton = ImageButton(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setImageResource(R.drawable.export)
            background = ContextCompat.getDrawable(context, android.R.drawable.ic_menu_edit)
            setOnClickListener {
                showAgreementOptions()
                statusLayout.visibility = View.GONE
                agreementType = AgreementType.NONE
            }
        }

        statusLayout.addView(iconView)
        statusLayout.addView(textContainer)
        statusLayout.addView(changeButton)

        // Add to parent container
        val parentContainer = findViewById<LinearLayout>(R.id.ll_rental_agreement_container)
        parentContainer?.addView(statusLayout)
    }

    /**
     * Show dialog to create custom agreement
     */
    private fun showCreateAgreementDialog() {
        // This will be implemented in future
        Toast.makeText(this, "Create Agreement feature coming soon", Toast.LENGTH_SHORT).show()

        // For now, we'll just simulate the creation process
        // In a real app, this would open a form or navigate to a new activity
        agreementType = AgreementType.CUSTOM
        hideAgreementOptions()
        showAgreementStatus("Custom Agreement Created",
            "Your customized rental agreement will be used",
            R.drawable.export)
    }

    /**
     * Update the UI with the selected rental agreement document.
     */
    private fun updateAgreementUI(documentUri: Uri) {
        // Check if the required views are available
        if (!::binding.isInitialized || binding.llAgreementDocument == null) {
            Toast.makeText(this, "Document viewer not available", Toast.LENGTH_SHORT).show()
            return
        }

        // Set the document thumbnail based on file type
        val fileName = CommonFunction().getFileName(this, documentUri)
        val fileExtension = fileName.substringAfterLast('.', "")

        if (fileExtension.equals("pdf", ignoreCase = true)) {
            binding.llAgreementDocument.ivThumbnail.setImageResource(R.drawable.export)
        } else {
            // For image files, try to set the thumbnail
            binding.llAgreementDocument.ivThumbnail.setImageURI(documentUri)
        }

        binding.llAgreementDocument.tvFileName.text = fileName
        val fileSize = CommonFunction().getFileSize(this, documentUri)
        binding.llAgreementDocument.tvFileSize.text = fileSize

        agreementDocument = documentUri
        agreementType = AgreementType.UPLOADED

        hideAgreementOptions()
        binding.llAgreementDocument.root.visibility = View.VISIBLE
    }

    // Rest of your existing code...
    // setupRentHouseLayout, setupLandDetailLayout, setupPgRoomsLayout, setupApartmentLayout, etc.

    /**
     * Opens the document picker for selecting PDF or image files for rental agreement.
     */
    private fun openDocumentPicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("application/pdf", "image/*"))
        }
        documentPickerLauncher.launch(intent.toString())
    }


    /**
     * Initialize the Rent House layout.
     */
    private fun setupRentHouseLayout() {
        setupRoomTypeSpinner()
    }

    /**
     * Initialize the Land Detail layout.
     */
    private fun setupLandDetailLayout() {
        setupLandAreaTypeSpinner()
    }

    /**
     * Initialize the PG Rooms layout.
     */
    private fun setupPgRoomsLayout() {
        binding.llPgRooms.incRoom.setOnClickListener {
            if (currentNumber < 99) {
                currentNumber++
                binding.llPgRooms.etRoomNumber.setText(String.format("%02d", currentNumber))
            }
        }
        binding.llPgRooms.decRoom.setOnClickListener {
            if (currentNumber > 0) {
                currentNumber--
                binding.llPgRooms.etRoomNumber.setText(String.format("%02d", currentNumber))
            }
        }
        binding.llPgRooms.incBeds.setOnClickListener {
            if (currentNumber < 99) {
                currentNumber++
                binding.llPgRooms.etBedNumber.setText(String.format("%02d", currentNumber))
            }
        }
        binding.llPgRooms.decBeds.setOnClickListener {
            if (currentNumber > 0) {
                currentNumber--
                binding.llPgRooms.etBedNumber.setText(String.format("%02d", currentNumber))
            }
        }
    }

    /**
     * Initialize the Apartment layout.
     */
    private fun setupApartmentLayout() {
        binding.llAppartment.buttonUp.setOnClickListener {
            if (currentNumber < 99) {
                currentNumber++
                binding.llAppartment.etNumber.setText(String.format("%02d", currentNumber))
            }
        }
        binding.llAppartment.buttonDown.setOnClickListener {
            if (currentNumber > 0) {
                currentNumber--
                binding.llAppartment.etNumber.setText(String.format("%02d", currentNumber))
            }
        }
        setupFlatTypeSpinner()
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
     * Setup the Room Type spinner for Rent House layout.
     */
    private fun setupRoomTypeSpinner() {
        val spinner = findViewById<Spinner>(R.id.spinner_room_type)
        val roomTypes = listOf("Select Room Type", "BK", "1BHK", "2BHK", "3BHK", "4BHK")
        setupSpinner(spinner, roomTypes)
    }

    /**
     * Setup the Flat Type spinner for Apartment layout.
     */
    private fun setupFlatTypeSpinner() {
        val spinner = findViewById<Spinner>(R.id.spinner_flat_type)
        val flatTypes = listOf("Select Flat Type", "1BHK", "2BHK", "3BHK", "4BHK")
        setupSpinner(spinner, flatTypes)
    }

    /**
     * Setup the Land Area Type spinner for Land Detail layout.
     */
    private fun setupLandAreaTypeSpinner() {
        val spinner = findViewById<Spinner>(R.id.spinner_area_type)
        val areaTypes = listOf("Sqft", "Sqmt")
        setupSpinner(spinner, areaTypes, defaultTextColor = Color.BLACK, selectedTextColor = Color.BLACK)
    }

    /**
     * Setup the Property Type spinner and update UI sections based on selection.
     */
    private fun setupPropertyTypeSpinner() {
        val spinner = findViewById<Spinner>(R.id.spinner_property_type)
        val propertyTypes = listOf("Select Property Type", "House", "Apartment", "PG", "Rent House", "Land")
        setupSpinner(spinner, propertyTypes)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                (view as? TextView)?.setTextColor(if (position == 0) Color.GRAY else Color.BLACK)
                updatePropertySection(position)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        spinner.setSelection(0)
    }

    /**
     * Update the visibility of UI sections based on the selected property type.
     */
    private fun updatePropertySection(selectedPosition: Int) {
        when (selectedPosition) {
            1 -> { // House
                binding.llHomeNumber.visibility = View.VISIBLE
                llApartment.visibility = View.GONE
                llPG.visibility = View.GONE
                llLand.visibility = View.GONE
                llRentHouse.visibility = View.GONE
            }
            2 -> { // Apartment
                llApartment.visibility = View.VISIBLE
                binding.llHomeNumber.visibility = View.GONE
                llPG.visibility = View.GONE
                llLand.visibility = View.GONE
                llRentHouse.visibility = View.GONE
            }
            3 -> { // PG
                llPG.visibility = View.VISIBLE
                binding.llHomeNumber.visibility = View.GONE
                llApartment.visibility = View.GONE
                llLand.visibility = View.GONE
                llRentHouse.visibility = View.GONE
            }
            4 -> { // Rent House
                llRentHouse.visibility = View.VISIBLE
                binding.llHomeNumber.visibility = View.GONE
                llApartment.visibility = View.GONE
                llPG.visibility = View.GONE
                llLand.visibility = View.GONE
            }
            5 -> { // Land
                llLand.visibility = View.VISIBLE
                binding.llHomeNumber.visibility = View.GONE
                llApartment.visibility = View.GONE
                llPG.visibility = View.GONE
                llRentHouse.visibility = View.GONE
            }
            else -> { // Default: No section selected
                binding.llHomeNumber.visibility = View.GONE
                llApartment.visibility = View.GONE
                llPG.visibility = View.GONE
                llLand.visibility = View.GONE
                llRentHouse.visibility = View.GONE
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


    // ActivityResultLaunchers for Camera, Gallery, and Document picker
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

    private val documentPickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                agreementDocument = uri
                updateAgreementUI(uri)
            } ?: Toast.makeText(this, "Failed to upload document!", Toast.LENGTH_SHORT).show()
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
        val photoFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
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