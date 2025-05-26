package com.gigi.restaurant.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlacesResponse(
    @field:Json(name = "features")
    val features: List<PlaceFeature>
)
@JsonClass(generateAdapter = true)
data class PlaceFeature(
    @field:Json(name = "properties")
    val properties: PlaceProperties
)

@JsonClass(generateAdapter = true)
data class PlaceProperties(
    @field:Json(name = "place_id") val placeId: String,
    @field:Json(name = "name")     val name: String?,
    @field:Json(name = "distance") val distance: Double?,
    @field:Json(name = "categories") val categories: List<String>

)
