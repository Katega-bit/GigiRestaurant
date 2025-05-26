package com.gigi.restaurant.presentation.ui.restaurants.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gigi.restaurant.data.local.RestaurantEntity
import com.gigi.restaurant.domain.model.RestaurantDetail
import com.gigi.restaurant.domain.repository.RestaurantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantDetailViewModel @Inject constructor(
    private val repository: RestaurantRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val id: String = checkNotNull(savedStateHandle["id"])

    private val distance: Double =
        (savedStateHandle.get<Float>("distance") ?: 0f).toDouble()
    private val _detail = MutableStateFlow<RestaurantDetail?>(null)
    val detail: StateFlow<RestaurantDetail?> = _detail.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val isFav: StateFlow<Boolean> = repository
        .isFavorite(id)
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    init {
        loadDetail()
    }

    fun loadDetail() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _detail.value = repository.getRestaurantDetail(id,  distance = distance)
            } catch (e: Exception) {
                Log.e("DetailVM", "Error cargando detalle", e)
                _error.value = "No se pudo cargar el detalle."
            }
            _isLoading.value = false
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            _detail.value?.let { d ->
                val entity = RestaurantEntity(d.id, d.name, d.distance)
                val currently = isFav.value
                repository.toggleFavorite(entity, currently)
            }
        }
    }
}

