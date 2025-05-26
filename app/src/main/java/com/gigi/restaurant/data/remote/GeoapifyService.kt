package com.gigi.restaurant.data.remote

import com.gigi.restaurant.BuildConfig
import com.gigi.restaurant.data.remote.dto.PlaceDetailsResponse
import com.gigi.restaurant.data.remote.dto.PlacesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoapifyService {

    @GET("v2/places")
    suspend fun getNearbyRestaurants(
        @Query("categories") categories: String = "catering.restaurant",
        @Query("filter") filter: String,
        @Query("limit") limit: Int = 20,
        @Query("bias") bias: String,
        @Query("apiKey") apiKey: String = BuildConfig.GEOAPIFY_API_KEY
    ): PlacesResponse

    @GET("v2/place-details")
    suspend fun getPlaceDetails(
        @Query("id") id: String,
        @Query("features") features: String = "details",
        @Query("apiKey") apiKey: String = BuildConfig.GEOAPIFY_API_KEY
    ): PlaceDetailsResponse
}
