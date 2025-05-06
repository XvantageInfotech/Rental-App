package com.xvantage.rental.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data class representing a property.
 */
@Parcelize
data class Property(
    val id: String = "", // Or Int, depending on API, default for when creating new
    val name: String = "",
    val address: String = "",
    val noOfRoom: Int = 0,
    val propertyTypeId: String = "", // Or Int
    val wa_number: String = "",
    val propertyImage: String? = null, // URL or local path
    // Add other relevant fields from your API if any
    val typeName: String? = null // Optional: To display in details if needed
) : Parcelable