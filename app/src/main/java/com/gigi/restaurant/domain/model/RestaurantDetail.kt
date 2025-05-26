package com.gigi.restaurant.domain.model


data class RestaurantDetail(
    val id: String,
    val name: String,
    val address: String,
    val phone: String,
    val website: String,
    val distance: Double,
    val openingHours: String,
    val imageUrl: String?,
    val lat: Double,
    val lon: Double
)
