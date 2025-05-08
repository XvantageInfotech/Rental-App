package com.xvantage.rental.network.response

/**
 * Project: Rental App By XV Team
 * Author: Mujammil x Vipul x XV Team
 * Date:  06/05/25
 * <p>
 * Licensed under the Apache License, Version 2.0. See LICENSE file for terms.
 */

data class PropertyType(
    val id: String,
    val name: String,
    val room_type: List<RoomType>
)
data class RoomType(val id: String, val name: String, val property_type_fk: String)