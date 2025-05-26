package com.gigi.restaurant.domain.model

data class Restaurant(
    val id: String,
    val name: String,
    val distance: Double,
    val categories: List<String>
)
