package com.xvantage.rental.data.source

data class Tenant(
    val id: String,              // Unique identifier for the tenant
    val tenantName: String,      // Tenant's name
    val tenantEmail: String,     // Tenant's email
    val moveInDate: String,      // Move-in date of the tenant
    val moveOutDate: String?,    // Move-out date (nullable if still residing)
    val contactInfo: String      // Tenant's contact information
)