package com.xvantage.rental.ui.addProperty

/**
 * Project: Rental App By XV Team
 * Author: Mujammil x Vipul x XV Team
 * Date:  24/04/25
 * <p>
 * Licensed under the Apache License, Version 2.0. See LICENSE file for terms.
 */

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap

/**
 * Repository to handle all property-related data operations.
 * In a real app, this would interact with a database, API, or other data sources.
 */
class PropertyRepository private constructor() {

    // In-memory storage for demo purposes
    private val properties = ConcurrentHashMap<String, Property>()
    private val rooms = ConcurrentHashMap<String, MutableList<Room>>()
    private val tenants = ConcurrentHashMap<String, MutableList<Tenant>>()
    private val financials = ConcurrentHashMap<String, PropertyFinancials>()

    /**
     * Gets a property by its ID
     */
    suspend fun getPropertyById(propertyId: String): Property {
        return withContext(Dispatchers.IO) {
            // Simulate network delay
            delay(500)

            properties[propertyId] ?: throw Exception("Property not found")
        }
    }

    /**
     * Gets all rooms for a specific property
     */
    suspend fun getRoomsByPropertyId(propertyId: String): List<Room> {
        return withContext(Dispatchers.IO) {
            // Simulate network delay
            delay(300)

            rooms[propertyId] ?: emptyList()
        }
    }

    /**
     * Gets all tenants for a specific property
     */
    suspend fun getTenantsByPropertyId(propertyId: String): List<Tenant> {
        return withContext(Dispatchers.IO) {
            // Simulate network delay
            delay(300)

            tenants[propertyId] ?: emptyList()
        }
    }

    /**
     * Gets financial data for a specific property
     */
    suspend fun getPropertyFinancials(propertyId: String): PropertyFinancials {
        return withContext(Dispatchers.IO) {
            // Simulate network delay
            delay(400)

            financials[propertyId] ?: PropertyFinancials(propertyId)
        }
    }

    /**
     * Adds a new property
     */
    suspend fun addProperty(property: Property): Property {
        return withContext(Dispatchers.IO) {
            // Simulate network delay
            delay(500)

            properties[property.id] = property

            // Initialize empty collections for this property
            rooms[property.id] = mutableListOf()
            tenants[property.id] = mutableListOf()
            financials[property.id] = PropertyFinancials(property.id)

            property
        }
    }

    /**
     * Adds a new room to a property
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun addRoom(room: Room): Room {
        return withContext(Dispatchers.IO) {
            // Simulate network delay
            delay(300)

            // Get the rooms for this property or create a new list
            val propertyRooms = rooms.getOrPut(room.propertyId) { mutableListOf() }

            // Add the room
            propertyRooms.add(room)

            // Update financials when adding room
            updateFinancialsForProperty(room.propertyId)

            room
        }
    }

    /**
     * Adds a new tenant to a property
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun addTenant(tenant: Tenant): Tenant {
        return withContext(Dispatchers.IO) {
            // Simulate network delay
            delay(300)

            // Get the tenants for this property or create a new list
            val propertyTenants = tenants.getOrPut(tenant.propertyId) { mutableListOf() }

            // Add the tenant
            propertyTenants.add(tenant)

            // Update financials when adding tenant
            updateFinancialsForProperty(tenant.propertyId)

            tenant
        }
    }

    /**
     * Updates a room
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun updateRoom(room: Room): Room {
        return withContext(Dispatchers.IO) {
            // Simulate network delay
            delay(300)

            val propertyRooms = rooms[room.propertyId] ?: throw Exception("Property not found")

            // Find and update the room
            val index = propertyRooms.indexOfFirst { it.id == room.id }
            if (index != -1) {
                propertyRooms[index] = room
                updateFinancialsForProperty(room.propertyId)
            } else {
                throw Exception("Room not found")
            }

            room
        }
    }

    /**
     * Updates a tenant
     */
    suspend fun updateTenant(tenant: Tenant): Tenant {
        return withContext(Dispatchers.IO) {
            // Simulate network delay
            delay(300)

            val propertyTenants = tenants[tenant.propertyId] ?: throw Exception("Property not found")

            // Find and update the tenant
            val index = propertyTenants.indexOfFirst { it.id == tenant.id }
            if (index != -1) {
                propertyTenants[index] = tenant
            } else {
                throw Exception("Tenant not found")
            }

            tenant
        }
    }

    /**
     * Deletes a room
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun deleteRoom(roomId: String, propertyId: String): Boolean {
        return withContext(Dispatchers.IO) {
            // Simulate network delay
            delay(300)

            val propertyRooms = rooms[propertyId] ?: return@withContext false

            // Remove the room
            val removed = propertyRooms.removeIf { it.id == roomId }

            if (removed) {
                // Also remove any tenants in this room
                val propertyTenants = tenants[propertyId]
                propertyTenants?.removeIf { it.roomId == roomId }

                // Update financials
                updateFinancialsForProperty(propertyId)
            }

            removed
        }
    }

    /**
     * Deletes a tenant
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun deleteTenant(tenantId: String, propertyId: String): Boolean {
        return withContext(Dispatchers.IO) {
            // Simulate network delay
            delay(300)

            val propertyTenants = tenants[propertyId] ?: return@withContext false

            // Remove the tenant
            val removed = propertyTenants.removeIf { it.id == tenantId }

            if (removed) {
                // Update financials
                updateFinancialsForProperty(propertyId)
            }

            removed
        }
    }

    /**
     * Updates financial calculations for a property based on rooms and tenants
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun updateFinancialsForProperty(propertyId: String) {
        withContext(Dispatchers.IO) {
            val propertyRooms = rooms[propertyId] ?: mutableListOf()
            val propertyTenants = tenants[propertyId] ?: mutableListOf()

            // Calculate total income (sum of monthly rates for rooms with tenants)
            var totalIncome = 0.0

            // Get unique list of occupied rooms (rooms that have tenants)
            val occupiedRoomIds = propertyTenants.map { it.roomId }.distinct()

            // Sum monthly rates for occupied rooms
            propertyRooms.forEach { room ->
                if (room.id in occupiedRoomIds) {
                    totalIncome += room.rent
                }
            }

            // For demo purposes, we'll set expenses as 30% of income
            val totalExpenses = totalIncome * 0.3

            // Simple monthly revenue
            val monthlyRevenue = mapOf(
                "January" to totalIncome * 0.9,
                "February" to totalIncome * 0.95,
                "March" to totalIncome * 1.0,
                "April" to totalIncome * 1.05,
                "May" to totalIncome * 1.1,
                "June" to totalIncome * 1.05
            )

            // Update financials
            val newFinancials = PropertyFinancials(
                propertyId = propertyId,
                totalIncome = totalIncome,
                totalExpenses = totalExpenses,
                monthlyRevenue = monthlyRevenue,
                lastUpdated = java.time.LocalDateTime.now().toString()
            )

            financials[propertyId] = newFinancials
        }
    }

    // Add some demo data for testing

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun addDemoData() {
        // Create a property
        val property = Property(
            id = "prop1",
            name = "Sunshine Apartments",
            type = "Apartment Complex",
            address = "123 Main St, Anytown, CA 94101",
            imageUrl = "https://example.com/property.jpg"
        )
        addProperty(property)

        // Updated room additions with consistent field names
        addRoom(Room(
            id = "room1",
            propertyId = "prop1",
            number = "101", // Updated from 'name' to 'number'
            type = "1BHK", // Added type
            rent = 1500.0, // MonthlyRate ➝ rent
            isOccupied = true // Status ➝ isOccupied
        ))
        addRoom(Room(
            id = "room2",
            propertyId = "prop1",
            number = "102",
            type = "Studio",
            rent = 1400.0,
            isOccupied = false
        ))
        addRoom(Room(
            id = "room3",
            propertyId = "prop1",
            number = "103",
            type = "2BHK",
            rent = 1700.0,
            isOccupied = true
        ))

        // Updated tenant additions with consistent field names
        addTenant(Tenant(
            id = "tenant1",
            propertyId = "prop1",
            roomId = "room1",
            tenantName = "John Doe",
            roomName = "555-1234",
            rentStartDate = "2023-01-01",
            roomDeposit = 3000.0 ,// Added deposit,
            tenantPhotoUri = "555-1234",
            aadhaarPhotoUri = "john.doe@example.com",
            rentSubmissionDate = "2024-01-01",

        ))
        addTenant(Tenant(
            id = "tenant1",
            propertyId = "prop1",
            roomId = "room1",
            tenantName = "John Doe",
            roomName = "555-1234",
            rentStartDate = "2023-01-01",
            roomDeposit = 3000.0 ,// Added deposit,
            tenantPhotoUri = "555-1234",
            aadhaarPhotoUri = "john.doe@example.com",
            rentSubmissionDate = "2024-01-01",

            ))
    }


    companion object {
        @Volatile
        private var instance: PropertyRepository? = null

        fun getInstance(): PropertyRepository {
            return instance ?: synchronized(this) {
                instance ?: PropertyRepository().also { instance = it }
            }
        }
    }
}