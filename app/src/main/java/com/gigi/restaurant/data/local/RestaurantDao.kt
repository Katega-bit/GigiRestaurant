package com.gigi.restaurant.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantDao {


    @Query("SELECT * FROM favorites")
    fun getFavorites(): Flow<List<RestaurantEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(entity: RestaurantEntity)

    @Delete
    fun deleteFavorite(entity: RestaurantEntity)


    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE id = :id)")
    fun isFavorite(id: String): Flow<Boolean>



}
