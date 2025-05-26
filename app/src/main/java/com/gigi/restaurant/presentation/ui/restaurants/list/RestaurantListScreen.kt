package com.gigi.restaurant.presentation.ui.restaurants.list

import android.Manifest
import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gigi.restaurant.presentation.ui.components.PullRefreshWrapper
import com.gigi.restaurant.presentation.ui.components.RestaurantListItem

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState


@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RestaurantListScreen(
    viewModel: RestaurantListViewModel,
    onFavoritesClick: () -> Unit,
    onSelect: (String, Double) -> Unit
) {
    val list = viewModel.restaurants.collectAsState().value
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val context = LocalContext.current

    val streetName by viewModel.streetName.collectAsState()

    val permState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)


    LaunchedEffect(Unit) {
        if (!permState.status.isGranted) {
            permState.launchPermissionRequest()
        }
    }

    LaunchedEffect(permState.status.isGranted) {
        if (permState.status.isGranted && list.isEmpty()) {
            viewModel.loadNearby()

        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Restaurantes Cercanos") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onFavoritesClick) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Ver favoritos"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->

        PullRefreshWrapper(
            isRefreshing = isRefreshing,
            onRefresh = {viewModel.loadNearby()},
            modifier = Modifier.fillMaxSize(),

        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Text(
                    text = "Estás en: $streetName",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.W600,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                Spacer(Modifier.height(14.dp))
                when {
                    isLoading -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    list.isEmpty() -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("No se han encontrado restaurantes.")
                        }
                    }
                    else -> {

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                top = 16.dp,
                                bottom = 42.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(12.dp),

                            ) {
                            items(list, key = { it.id } ) { r ->
                                val isFav = favoriteIds.contains(r.id)
                                RestaurantListItem(
                                    restaurant       = r,
                                    isFavorite       = isFav,
                                    onClick          = { onSelect(r.id, r.distance) },
                                    onToggleFavorite = { viewModel.toggleFavorite(r) }
                                )
                            }
                        }
                    }
                }

            }
        }



        }
    }

