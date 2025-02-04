package com.xvantage.rental.data.source.sample

data class Room(
    val id: String,
    val roomName: String,
    val size: String,
    val tenant: Tenant? = null,
    val occupied: Boolean = tenant != null, // New flag
    val createdAt: String,
    val updatedAt: String
)
