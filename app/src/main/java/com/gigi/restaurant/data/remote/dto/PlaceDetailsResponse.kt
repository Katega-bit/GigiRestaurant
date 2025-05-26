package com.gigi.restaurant.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class PlaceDetailsResponse(
    @field:Json(name = "properties")
    val rootProperties: PlaceDetailsProperties? = null,

    @field:Json(name = "features")
    val features: List<PlaceDetailsFeature>? = null
)

@JsonClass(generateAdapter = true)
data class PlaceDetailsFeature(
    @field:Json(name = "properties")
    val properties: PlaceDetailsProperties
)

@JsonClass(generateAdapter = true)
data class PlaceDetailsProperties(
    @field:Json(name = "place_id")      val placeId: String,
    @field:Json(name = "name")          val name: String?,
    @field:Json(name = "address_line1") val address1: String?,
    @field:Json(name = "address_line2") val address2: String?,
    @field:Json(name = "city") val city: String?,
    @field:Json(name = "street") val street: String?,
    @field:Json(name = "housenumber") val housenumber: String?,
    @field:Json(name = "distance")          val distance: Int?,
    @field:Json(name = "lat") val latitude: Double,
    @field:Json(name = "lon") val longitude: Double,



    @field:Json(name = "contact")
    val contact: ContactInfo? = null,
    @field:Json(name = "wiki_and_media")
    val wikiAndMedia: WikiAndMedia? = null,

    @field:Json(name = "opening_hours")       val openingHours: String?,
    @field:Json(name = "website")       val website: String?
)

@JsonClass(generateAdapter = true)
data class ContactInfo(
    @field:Json(name = "phone")                    val phone: String? = null,
    @field:Json(name = "phone_other")              val phoneOther: List<String>? = null,
    @field:Json(name = "phone_international")      val phoneInternational: Map<String, String>? = null,
    @field:Json(name = "email")                    val email: String? = null,
    @field:Json(name = "email_other")              val emailOther: List<String>? = null,
    @field:Json(name = "fax")                      val fax: String? = null
)

@JsonClass(generateAdapter = true)
data class WikiAndMedia(
    @field:Json(name = "wikidata")            val wikidata: String? = null,
    @field:Json(name = "wikipedia")           val wikipedia: String? = null,
    @field:Json(name = "wikimedia_commons")   val wikimediaCommons: String? = null,
    @field:Json(name = "image")               val image: String? = null
)
