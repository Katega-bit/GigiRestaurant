package com.gigi.restaurant.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.gigi.restaurant.presentation.ui.restaurants.detail.RestaurantDetailScreen
import com.gigi.restaurant.presentation.ui.restaurants.favorites.FavoritesScreen
import com.gigi.restaurant.presentation.ui.restaurants.list.RestaurantListScreen
import com.gigi.restaurant.presentation.ui.restaurants.list.RestaurantListViewModel

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "restaurant_list"
    ) {
        composable("restaurant_list") {
            val vm: RestaurantListViewModel = hiltViewModel()
            RestaurantListScreen(
                viewModel = vm,
                onFavoritesClick = { navController.navigate("favorites") },
                onSelect = { id, distance -> navController.navigate("detail/$id/${distance.toFloat()}") }
            )
        }
        composable("favorites") {
            FavoritesScreen(
                onSelect = { id, distance -> navController.navigate("detail/$id/${distance.toFloat()}") },
                viewModel = hiltViewModel(),
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "detail/{id}/{distance}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType } ,
                navArgument("distance") { type = NavType.FloatType }

            )

        ) {
            RestaurantDetailScreen(
                viewModel = hiltViewModel(),
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
