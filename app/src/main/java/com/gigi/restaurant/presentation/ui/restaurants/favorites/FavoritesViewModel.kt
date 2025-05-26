package com.gigi.restaurant.presentation.ui.restaurants.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gigi.restaurant.data.local.RestaurantEntity
import com.gigi.restaurant.domain.repository.RestaurantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: RestaurantRepository
) : ViewModel() {

    val favorites: StateFlow<List<RestaurantEntity>> =
        repository.getFavorites()
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}
