package com.xvantage.rental.network.response

import com.google.gson.annotations.SerializedName

/**
 * Project: Rental App By XV Team
 * Author: Mujammil x Vipul x XV Team
 * Date:  06/05/25
 * <p>
 * Licensed under the Apache License, Version 2.0. See LICENSE file for terms.
 */

data class CreatePropertyResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: Data,
    val err: Any?
) {
    data class Data(
        val id: String,
        val status: Boolean,
        @SerializedName("created_at") val createdAt: String,
        @SerializedName("updated_at") val updatedAt: String,
        val name: String,
        val address: String,
        @SerializedName("no_of_room") val noOfRoom: String,
        @SerializedName("type_fk") val typeFk: TypeFk,
        @SerializedName("user_fk") val userFk: String,
        @SerializedName("wa_number") val waNumber: String,
        @SerializedName("room_details") val roomDetails: List<Any>,
        @SerializedName("owner_name") val ownerName: String
    ) {
        data class TypeFk(
            val id: String,
            val name: String
        )
    }
}