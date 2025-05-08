package com.xvantage.rental.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data class representing a tenant.
 */
@Parcelize
data class Tenant(
    val id: String = "", // Or Int, depending on API, default for new
    val roomId: String = "", // Or Int
    val tenantName: String = "",
    val phoneNumber: String = "",
    val phoneCode: String = "",
    val roomDeposit: Double = 0.0,
    val checkinDate: String = "", // Consider using a Date/Timestamp type
    val rentStartDate: String = "", // Consider using a Date/Timestamp type
    val rentSubmissionDate: String = "", // Consider using a Date/Timestamp type
    val profilePic: String? = null, // URL or local path
    // Add other relevant fields from your API if any
    val propertyId: String? = null // Optional: if needed for some queries or display
) : Parcelable