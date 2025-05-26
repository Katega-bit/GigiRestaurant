package com.gigi.restaurant.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class RestaurantEntity(
    @PrimaryKey val id: String,
    val name: String,
    val distance: Double
)
