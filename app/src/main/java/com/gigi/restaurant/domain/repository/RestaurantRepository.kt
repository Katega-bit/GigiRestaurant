package com.gigi.restaurant.domain.repository

import com.gigi.restaurant.data.local.RestaurantEntity
import com.gigi.restaurant.domain.model.Restaurant
import com.gigi.restaurant.domain.model.RestaurantDetail
import kotlinx.coroutines.flow.Flow

interface RestaurantRepository {
    suspend fun getNearbyRestaurants(lat: Double, lon: Double): List<Restaurant>

    suspend fun getRestaurantDetail(id: String, distance: Double): RestaurantDetail

    fun getFavorites(): Flow<List<RestaurantEntity>>
    suspend fun toggleFavorite(entity: RestaurantEntity, isFav: Boolean)
    fun isFavorite(id: String): Flow<Boolean>
    fun favoriteIdsFlow(): Flow<Set<String>>

}
