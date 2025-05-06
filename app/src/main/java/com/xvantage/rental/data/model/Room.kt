package com.xvantage.rental.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data class representing a room within a property.
 */
@Parcelize
data class Room(
    val id: String = "", // Or Int, depending on API, default for new
    val propertyId: String = "", // Or Int
    val roomNo: String = "",
    val roomTypeText: String = "",
    val address: String = "", // Can be same as property or specific
    val roomImage: String? = null, // URL or local path
    val roomTypeId: String = "", // Or Int, maps to a RoomType entity if you have one
    val rent: Double = 0.0,
    val meterReading: String? = null,
    val meterReadingLastDate: String? = null, // Consider using a Date/Timestamp type
    val propertyTypeId: String = "", // Or Int, (already in AddRoomBottomSheet inputs)
    // Fields for UI display in RoomsFragment
    val occupied: Boolean = false, // You might need to derive this or get from API
    val tenantName: String? = null // If occupied, the name of the tenant
) : Parcelable