package com.gigi.restaurant.presentation.ui.components

import androidx.annotation.DrawableRes
import com.gigi.restaurant.R

@DrawableRes
fun getImageForCategory(categories: List<String>): Int {
    return when {
        "catering.restaurant.spanish" in categories -> R.drawable.category_spanish
        "catering.restaurant.sandwich" in categories -> R.drawable.category_sandwich
        "catering.restaurant.steak_house" in categories -> R.drawable.category_steak
        "catering.restaurant.italian" in categories -> R.drawable.category_italian
        "catering.restaurant.burger" in categories -> R.drawable.category_burguer
        "catering.restaurant.argentinian" in categories -> R.drawable.category_argentinian
        "catering.restaurant.regional" in categories -> R.drawable.category_regional
        "catering.restaurant.ramen" in categories -> R.drawable.category_japanise

        else -> R.drawable.category_restaurant
    }
}
