package com.gigi.restaurant.data.remote.mapper

import com.gigi.restaurant.data.remote.dto.PlaceFeature
import com.gigi.restaurant.data.remote.dto.PlaceDetailsResponse
import com.gigi.restaurant.domain.model.Restaurant
import com.gigi.restaurant.domain.model.RestaurantDetail


fun PlaceFeature.toDomain(): Restaurant =
    Restaurant(
        id = properties.placeId,
        name = properties.name.orEmpty(),
        distance = properties.distance ?: 0.0,
        categories = properties.categories
    )


fun PlaceDetailsResponse.toDomainDetail(defaultDistance: Double): RestaurantDetail {
    val props = features
        ?.firstOrNull()
        ?.properties
        ?: throw IllegalStateException("No se encontró ninguna feature en PlaceDetailsResponse")

    val phoneNumber = props.contact?.phone.orEmpty()
    val image = props.wikiAndMedia?.image

    return RestaurantDetail(
        id       = props.placeId,
        name     = props.name.orEmpty(),
        address  = props.address2.orEmpty(),
        phone    = phoneNumber,
        website  = props.website.orEmpty(),
        distance = defaultDistance,
        openingHours = props.openingHours.orEmpty(),
        imageUrl  = image,
        lat       = props.latitude,
        lon       = props.longitude

    )
}

