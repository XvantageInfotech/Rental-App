package com.xvantage.rental.ui.addProperty.tempFiles

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

/**
 * Project: Rental App By XV Team
 * Author: Mujammil x Vipul x XV Team
 * Date:  24/04/25
 * <p>
 * Licensed under the Apache License, Version 2.0. See LICENSE file for terms.
 */

@Parcelize
data class Property(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val type: String = "",
    val address: String = "",
    val imageUrl: String = ""
) : Parcelable