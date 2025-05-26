package com.gigi.restaurant.presentation.ui.restaurants.list

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gigi.restaurant.data.local.RestaurantEntity
import com.gigi.restaurant.domain.model.Restaurant
import com.gigi.restaurant.domain.repository.RestaurantRepository
import com.gigi.restaurant.domain.usecase.GetCurrentLocationUseCase
import com.gigi.restaurant.domain.usecase.LatLon
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

@SuppressLint("MissingPermission")
@HiltViewModel
class RestaurantListViewModel @Inject constructor(
    private val repository: RestaurantRepository,
    @ApplicationContext private val context: Context,
    private val getLocation: GetCurrentLocationUseCase

) : ViewModel() {

    private val _restaurants = MutableStateFlow<List<Restaurant>>(emptyList())
    val restaurants: StateFlow<List<Restaurant>> = _restaurants

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _streetName = MutableStateFlow("Obteniendo calle…")
    val streetName: StateFlow<String> = _streetName

    private val _latLon = MutableStateFlow(LatLon(40.4168, -3.7038) )
    val latLon: StateFlow<LatLon> = _latLon

    fun toggleIsRefreshing(){
        _isRefreshing.value = true
    }

    val favoriteIds: StateFlow<Set<String>> = repository
        .favoriteIdsFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptySet()
        )

    fun loadNearby() = viewModelScope.launch {
        Log.d("VM", "▶ loadNearby arrancado")
        _isLoading.value = true
        _isRefreshing.value = true

        _latLon.value = getLocation() ?: run {
            val fallback = LatLon(40.4168, -3.7038)
            Log.w("VM", "Ubicación nula, usando fallback: $fallback")
            fallback
        }

        Log.d("VM", "Usando coords: lat=${latLon.value.lat}, lon=${latLon.value.lon}")
        val result = repository.getNearbyRestaurants(latLon.value.lat, latLon.value.lon)
        Log.d("VM", "Repositorio devolvió ${result.size} restaurantes")
        _restaurants.value = result


        val name = withContext(Dispatchers.IO) {
            try {

                val geo = Geocoder(context, Locale.getDefault())
                geo.getFromLocation(_latLon.value.lat, latLon.value.lon, 1)
                    ?.firstOrNull()
                    ?.thoroughfare
                    ?: geo.getFromLocation(_latLon.value.lat, _latLon.value.lat, 1)
                        ?.firstOrNull()
                        ?.getAddressLine(0)
                    ?: "Ubicación desconocida"
            } catch (e: Exception) {
                "No se pudo obtener la calle"
            }
        }
        _streetName.value = name

        _isLoading.value = false
        _isRefreshing.value = false

    }


    fun toggleFavorite(restaurant: Restaurant) {
        viewModelScope.launch {
            val currentlyFav = repository.isFavorite(restaurant.id).first()
            val entity = RestaurantEntity(
                id = restaurant.id,
                name = restaurant.name,
                distance = restaurant.distance
            )
            repository.toggleFavorite(entity, currentlyFav)
        }
    }

}
