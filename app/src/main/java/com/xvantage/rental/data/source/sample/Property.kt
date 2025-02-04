package com.xvantage.rental.data.source.sample

data class Property(
    val id: String,              // Unique identifier for the property
    val ownerName: String,       // Owner's name
    val propertyName: String,    // Name of the property (e.g., 'Villa 101')
    val address: String,         // Property address
    val rooms: List<Room>,       // List of rooms in the property
    val contactInfo: String,     // Owner's contact information
    val createdAt: String,       // Date when the property was created
    val updatedAt: String        // Last updated timestamp
)


fun getDummyProperties(): List<Property> {
    val tenant1 = Tenant(
        id = "tenant1",
        tenantName = "John Doe",
        tenantEmail = "johndoe@example.com",
        moveInDate = "2023-01-01",
        moveOutDate = null,
        contactInfo = "123-456-7890"
    )

    val tenant2 = Tenant(
        id = "tenant2",
        tenantName = "Jane Smith",
        tenantEmail = "janesmith@example.com",
        moveInDate = "2023-05-01",
        moveOutDate = null,
        contactInfo = "987-654-3210"
    )

    val tenant3 = Tenant(
        id = "tenant3",
        tenantName = "Robert Brown",
        tenantEmail = "robertbrown@example.com",
        moveInDate = "2023-02-15",
        moveOutDate = null,
        contactInfo = "555-000-1111"
    )

    val tenant4 = Tenant(
        id = "tenant4",
        tenantName = "Emily Johnson",
        tenantEmail = "emilyj@example.com",
        moveInDate = "2023-03-20",
        moveOutDate = null,
        contactInfo = "555-222-3333"
    )

    val tenant5 = Tenant(
        id = "tenant5",
        tenantName = "Michael Clark",
        tenantEmail = "michaelclark@example.com",
        moveInDate = "2023-04-10",
        moveOutDate = null,
        contactInfo = "555-444-5555"
    )

    val property1 = Property(
        id = "property1",
        ownerName = "Owner 1",
        propertyName = "Villa 101",
        address = "123 Villa Street",
        rooms = listOf(
            Room(
                id = "room1",
                roomName = "Bedroom",
                size = "15m²",
                tenant = tenant1,
                createdAt = "2023-01-01",
                updatedAt = "2023-01-10"
            ),
            Room(
                id = "room2",
                roomName = "Living Room",
                size = "25m²",
                tenant = tenant2,
                createdAt = "2023-01-01",
                updatedAt = "2023-01-10"
            )
        ),
        contactInfo = "owner1@example.com",
        createdAt = "2023-01-01",
        updatedAt = "2023-01-10"
    )

    val property2 = Property(
        id = "property2",
        ownerName = "Owner 2",
        propertyName = "Luxury Suite 202",
        address = "456 Luxury Road",
        rooms = listOf(
            Room(
                id = "room1",
                roomName = "Master Bedroom",
                size = "20m²",
                tenant = tenant3,
                createdAt = "2023-02-15",
                updatedAt = "2023-02-20"
            ),
            Room(
                id = "room2",
                roomName = "Kitchen",
                size = "18m²",
                tenant = null,
                createdAt = "2023-02-15",
                updatedAt = "2023-02-20"
            )
        ),
        contactInfo = "owner2@example.com",
        createdAt = "2023-02-01",
        updatedAt = "2023-02-20"
    )

    val property3 = Property(
        id = "property3",
        ownerName = "Owner 3",
        propertyName = "Garden Cottage 303",
        address = "789 Green Valley",
        rooms = listOf(
            Room(
                id = "room1",
                roomName = "Bedroom",
                size = "12m²",
                tenant = tenant4,
                createdAt = "2023-03-20",
                updatedAt = "2023-03-25"
            ),
            Room(
                id = "room2",
                roomName = "Bathroom",
                size = "8m²",
                tenant = null,
                createdAt = "2023-03-20",
                updatedAt = "2023-03-25"
            )
        ),
        contactInfo = "owner3@example.com",
        createdAt = "2023-03-01",
        updatedAt = "2023-03-25"
    )

    val property4 = Property(
        id = "property4",
        ownerName = "Owner 4",
        propertyName = "Beachfront Villa 404",
        address = "123 Ocean Drive",
        rooms = listOf(
            Room(
                id = "room1",
                roomName = "Bedroom",
                size = "22m²",
                tenant = null,
                createdAt = "2023-04-01",
                updatedAt = "2023-04-05"
            ),
            Room(
                id = "room2",
                roomName = "Living Room",
                size = "30m²",
                tenant = tenant5,
                createdAt = "2023-04-01",
                updatedAt = "2023-04-05"
            )
        ),
        contactInfo = "owner4@example.com",
        createdAt = "2023-04-01",
        updatedAt = "2023-04-05"
    )

    val property5 = Property(
        id = "property5",
        ownerName = "Owner 5",
        propertyName = "Downtown Loft 505",
        address = "987 Urban Street",
        rooms = listOf(
            Room(
                id = "room1",
                roomName = "Loft Bedroom",
                size = "25m²",
                tenant = tenant1,
                createdAt = "2023-05-01",
                updatedAt = "2023-05-10"
            ),
            Room(
                id = "room2",
                roomName = "Kitchen",
                size = "15m²",
                tenant = null,
                createdAt = "2023-05-01",
                updatedAt = "2023-05-10"
            )
        ),
        contactInfo = "owner5@example.com",
        createdAt = "2023-05-01",
        updatedAt = "2023-05-10"
    )

    val property6 = Property(
        id = "property6",
        ownerName = "Owner 6",
        propertyName = "Suburban House 606",
        address = "654 Suburbia Road",
        rooms = listOf(
            Room(
                id = "room1",
                roomName = "Bedroom",
                size = "18m²",
                tenant = tenant2,
                createdAt = "2023-06-10",
                updatedAt = "2023-06-15"
            ),
            Room(
                id = "room2",
                roomName = "Living Room",
                size = "28m²",
                tenant = null,
                createdAt = "2023-06-10",
                updatedAt = "2023-06-15"
            )
        ),
        contactInfo = "owner6@example.com",
        createdAt = "2023-06-01",
        updatedAt = "2023-06-15"
    )

    val property7 = Property(
        id = "property7",
        ownerName = "Owner 7",
        propertyName = "Luxury Condo 707",
        address = "321 Skyscraper Blvd",
        rooms = listOf(
            Room(
                id = "room1",
                roomName = "Bedroom",
                size = "14m²",
                tenant = tenant3,
                createdAt = "2023-07-10",
                updatedAt = "2023-07-15"
            ),
            Room(
                id = "room2",
                roomName = "Bathroom",
                size = "10m²",
                tenant = null,
                createdAt = "2023-07-10",
                updatedAt = "2023-07-15"
            )
        ),
        contactInfo = "owner7@example.com",
        createdAt = "2023-07-01",
        updatedAt = "2023-07-15"
    )

    val property8 = Property(
        id = "property8",
        ownerName = "Owner 8",
        propertyName = "Penthouse Suite 808",
        address = "456 High Tower Street",
        rooms = listOf(
            Room(
                id = "room1",
                roomName = "Master Bedroom",
                size = "30m²",
                tenant = tenant4,
                createdAt = "2023-08-01",
                updatedAt = "2023-08-05"
            ),
            Room(
                id = "room2",
                roomName = "Living Room",
                size = "40m²",
                tenant = tenant2,
                createdAt = "2023-08-01",
                updatedAt = "2023-08-05"
            )
        ),
        contactInfo = "owner8@example.com",
        createdAt = "2023-08-01",
        updatedAt = "2023-08-05"
    )

    val property9 = Property(
        id = "property9",
        ownerName = "Owner 9",
        propertyName = "Mountain Retreat 909",
        address = "852 Mountainview Lane",
        rooms = listOf(
            Room(
                id = "room1",
                roomName = "Bedroom",
                size = "12m²",
                tenant = tenant5,
                createdAt = "2023-09-01",
                updatedAt = "2023-09-05"
            ),
            Room(
                id = "room2",
                roomName = "Living Room",
                size = "18m²",
                tenant = null,
                createdAt = "2023-09-01",
                updatedAt = "2023-09-05"
            )
        ),
        contactInfo = "owner9@example.com",
        createdAt = "2023-09-01",
        updatedAt = "2023-09-05"
    )

    val property10 = Property(
        id = "property10",
        ownerName = "Owner 10",
        propertyName = "Suburban Villa 1010",
        address = "963 Oakwood Drive",
        rooms = listOf(
            Room(
                id = "room1",
                roomName = "Bedroom",
                size = "20m²",
                tenant = tenant1,
                createdAt = "2023-10-01",
                updatedAt = "2023-10-05"
            ),
            Room(
                id = "room2",
                roomName = "Living Room",
                size = "25m²",
                tenant = tenant3,
                createdAt = "2023-10-01",
                updatedAt = "2023-10-05"
            )
        ),
        contactInfo = "owner10@example.com",
        createdAt = "2023-10-01",
        updatedAt = "2023-10-05"
    )

    return listOf(
        property1,
        property2,
        property3,
        property4,
        property5,
        property6,
        property7,
        property8,
        property9,
        property10
    )
}

