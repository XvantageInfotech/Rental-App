package com.xvantage.rental.network.request.property

/**
 * Project: Rental App By XV Team
 * Author: Mujammil x Vipul x XV Team
 * Date:  19/04/25
 * <p>
 * Licensed under the Apache License, Version 2.0. See LICENSE file for terms.
 */

data class CreatePropertyRequest(
    val name: String,
    val ownerName: String,
    val address: String,
    val propertyType: String,
    val noOfRoom: String,
    val propertyImage: String,
    val propertyTypeId: String,
    val whatsappNumber: String,
    val roomNumber: List<String>
)
