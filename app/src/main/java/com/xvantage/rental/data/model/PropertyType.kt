package com.xvantage.rental.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data class representing a property type.
 */
@Parcelize
data class PropertyType(
    val typeId: String, // Or Int, depending on API
    val typeName: String
) : Parcelable