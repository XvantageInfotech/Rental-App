package com.xvantage.rental.ui.addProperty.bmsheet

import android.R
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xvantage.rental.databinding.FragmentAddRoomBottomSheetBinding
import com.xvantage.rental.ui.addProperty.tempFiles.Room
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class AddRoomBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddRoomBottomSheetBinding? = null
    private val binding get() = _binding!!

    private var onRoomAddedListener: ((Room) -> Unit)? = null
    private val calendar = Calendar.getInstance()
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    fun setOnRoomAddedListener(listener: (Room) -> Unit) {
        onRoomAddedListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddRoomBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRoomTypeSpinner()
        setupDatePicker()
        setupActionButtons()
    }

    private fun setupRoomTypeSpinner() {
        val roomTypes = arrayOf("1BHK", "2BHK", "3BHK", "Single Room", "Studio", "Other")
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_dropdown_item,
            roomTypes
        )
        binding.spinnerRoomType.adapter = adapter
    }

    private fun setupDatePicker() {
        binding.etReadingDate.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            updateDateField()
        }

        DatePickerDialog(
            requireContext(),
            dateListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateDateField() {
        binding.etReadingDate.setText(dateFormatter.format(calendar.time))
    }

    private fun setupActionButtons() {
        // Cancel button
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        // Save button
        binding.btnSave.setOnClickListener {
            if (validateFields()) {
                saveRoom()
            }
        }

        // Close button
        binding.ivClose.setOnClickListener {
            dismiss()
        }
    }

    private fun validateFields(): Boolean {
        var isValid = true

        // Validate room number/name
        if (binding.etRoomNumber.text.isNullOrBlank()) {
            binding.etRoomNumber.error = "Room number is required"
            isValid = false
        }

        // Validate rent
        if (binding.etRoomRent.text.isNullOrBlank()) {
            binding.etRoomRent.error = "Rent amount is required"
            isValid = false
        }

        return isValid
    }

    private fun saveRoom() {
        val roomNumber = binding.etRoomNumber.text.toString()
        val roomType = binding.spinnerRoomType.selectedItem.toString()
        val rentAmount = binding.etRoomRent.text.toString().toDoubleOrNull() ?: 0.0
        val meterReading = binding.etMeterReading.text.toString().toDoubleOrNull() ?: 0.0
        val readingDate = calendar.timeInMillis

        val newRoom = Room(
            id = UUID.randomUUID().toString(),
            propertyId = "", // This should be set by the parent activity
            number = roomNumber,
            type = roomType,
            rent = rentAmount,
            meterReading = meterReading,
            readingDate = readingDate,
            isOccupied = false
        )

        // Notify listener about the new room
        onRoomAddedListener?.invoke(newRoom)

        // Close bottom sheet
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}