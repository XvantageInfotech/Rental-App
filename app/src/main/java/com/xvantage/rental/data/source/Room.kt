package com.xvantage.rental.data.source


data class Room(
    val id: String,              // Unique identifier for the room
    val roomName: String,        // Name or type of the room (e.g., 'Bedroom', 'Living Room')
    val size: String,            // Size of the room (e.g., '20m²')
    val tenant: Tenant? = null,  // Tenant info for the room (null if no tenant)
    val createdAt: String,       // Date when the room was created
    val updatedAt: String        // Last updated timestamp
)