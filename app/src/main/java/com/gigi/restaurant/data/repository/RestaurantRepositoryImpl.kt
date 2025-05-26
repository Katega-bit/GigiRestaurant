package com.gigi.restaurant.data.repository
import com.gigi.restaurant.data.remote.mapper.toDomain
import android.util.Log
import com.gigi.restaurant.data.local.RestaurantDao
import com.gigi.restaurant.data.local.RestaurantEntity
import com.gigi.restaurant.data.remote.GeoapifyService
import com.gigi.restaurant.data.remote.mapper.toDomainDetail
import com.gigi.restaurant.domain.model.Restaurant
import com.gigi.restaurant.domain.model.RestaurantDetail
import com.gigi.restaurant.domain.repository.RestaurantRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RestaurantRepositoryImpl @Inject constructor(
    private val service: GeoapifyService,
    private val dao: RestaurantDao
) : RestaurantRepository {

    override suspend fun getNearbyRestaurants(lat: Double, lon: Double): List<Restaurant> {
        return try {
            val filter = "circle:$lon,$lat,5000"
            val bias   = "proximity:$lon,$lat"
            Log.d("Repository", "Fetch con lat=$lat lon=$lon")
            service.getNearbyRestaurants(filter = filter, bias = bias)
                .features
                .map { feature -> feature.toDomain() }  // ahora sí existe toDomain()
        } catch (e: retrofit2.HttpException) {
            Log.e("Repo", "Geoapify HTTP ${e.code()}: ${e.message()}")
            emptyList()
        } catch (e: IOException) {
            Log.e("Repo", "Network error: ${e.localizedMessage}")
            emptyList()
        }
    }


    override suspend fun getRestaurantDetail(id: String, distance: Double): RestaurantDetail {
        try {
            Log.d("Repo", "Detalle place_id=$id")
            val response = service.getPlaceDetails(id = id)
            val domain = response.toDomainDetail(defaultDistance = distance)
            Log.d("Repo", "Mapeado a dominio: $domain")
            return domain
        } catch (e: Exception) {
            Log.e("Repo", "Error en getRestaurantDetail: ${e.message}", e)
            throw e
        }
    }



    override fun getFavorites(): Flow<List<RestaurantEntity>> =
        dao.getFavorites()

    override suspend fun toggleFavorite(entity: RestaurantEntity, isFav: Boolean) {
        withContext(Dispatchers.IO) {
            if (isFav) dao.deleteFavorite(entity)
            else dao.insertFavorite(entity)
        }
    }

    override fun isFavorite(id: String): Flow<Boolean> =
        dao.isFavorite(id)

    override fun favoriteIdsFlow(): Flow<Set<String>> =
        dao.getFavorites()              // Flow<List<FavoriteEntity>>
            .map { list -> list.map { it.id }.toSet() }
}
