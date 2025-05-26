package com.gigi.restaurant.presentation.ui.restaurants.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.gigi.restaurant.R
import com.gigi.restaurant.domain.model.RestaurantDetail
import com.gigi.restaurant.presentation.ui.theme.MyBlue
import com.google.android.gms.location.places.Place

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailScreen(
    viewModel: RestaurantDetailViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val detail by viewModel.detail.collectAsState()
    val isFav  by viewModel.isFav.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error     by viewModel.error.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(detail?.name ?: "") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
            )
        }
    ) { inner ->
        Box(Modifier
            .fillMaxSize()
            .padding(inner)) {

            when {
                isLoading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                error != null -> {
                    Column(
                        Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(error!!, color = MaterialTheme.colorScheme.error)
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = { viewModel.loadDetail() }) {
                            Text("Reintentar")
                        }
                    }
                }
                detail != null -> {
                    DetailContent(
                        detail     = detail!!,
                        isFav      = isFav,
                        onToggleFav = { viewModel.toggleFavorite() },
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailContent(
    detail: RestaurantDetail,
    isFav: Boolean,
    onToggleFav: () -> Unit,
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AsyncImage(
            model = detail.imageUrl,
            contentDescription = detail.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(R.drawable.no_image),
            error = painterResource(R.drawable.category_restaurant)
        )
        Text(
            text = detail.name,
            style = MaterialTheme.typography.headlineMedium
        )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row() {
                    Icon(Icons.Default.Place, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "${detail.distance.toInt()} m",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                if (detail.openingHours.isNotBlank()){
                    Row(){
                        Icon(painterResource(R.drawable.ic_clock), contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = detail.openingHours ,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }


            }


        HorizontalDivider()

        Section(title = "Dirección") {
            Text(
                text = detail.address,
                color = MyBlue,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .clickable {

                        val uri =
                            "geo:${detail.lat},${detail.lon}?q=${Uri.encode(detail.address)}".toUri()
                        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                            setPackage("com.google.android.apps.maps")
                        }
                        context.startActivity(intent)
                    }
                    .padding(vertical = 8.dp)
            )
        }

        Section(title = "Teléfono") {
            Text(
                text = detail.phone,
                color = MyBlue,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .clickable {
                        val intent = Intent(
                            Intent.ACTION_DIAL,
                            "tel:${detail.phone}".toUri()
                        )
                        context.startActivity(intent)
                    }
                    .padding(vertical = 8.dp)
            )
        }

        Section(title = "Sitio Web") {
            Text(
                text = detail.website,
                style = MaterialTheme.typography.bodyLarge,
                color = MyBlue,
                modifier = Modifier
                    .clickable {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            detail.website.toUri()
                        )
                        context.startActivity(intent)
                    }
                    .padding(vertical = 8.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onToggleFav,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(
                imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = null
            )
            Spacer(Modifier.width(8.dp))
            Text(if (isFav) "Eliminar de favoritos" else "Añadir a favoritos")
        }
    }
}

@Composable
private fun Section(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        content()
    }
}


