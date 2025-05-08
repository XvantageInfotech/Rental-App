package com.xvantage.rental.network.request.property

import android.net.Uri

/**
 * Project: Rental App By XV Team
 * Author: Mujammil x Vipul x XV Team
 * Date:  19/04/25
 * <p>
 * Licensed under the Apache License, Version 2.0. See LICENSE file for terms.
 */

data class CreatePropertyRequest(
    val address: String,
    val noOfRoom: Int,
    val propertyTypeId: String,
    val wa_number: String,
    val name: String,
    val imageUri: Uri? = null
)   