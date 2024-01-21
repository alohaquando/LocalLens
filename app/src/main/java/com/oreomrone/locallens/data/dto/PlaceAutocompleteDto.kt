package com.oreomrone.locallens.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val latitude: Double,
    val longitude: Double
)

@Serializable
data class DisplayName(
    val text: String,
    @SerialName("languageCode")
    val languageCode: String? = null
)

@Serializable
data class PlaceAutocomplete(
    val id: String,
    @SerialName("formattedAddress")
    val formattedAddress: String,
    val location: Location,
    @SerialName("displayName")
    val displayName: DisplayName
)

@Serializable
data class PlacesResponse(
    @SerialName("places") val places: List<PlaceAutocomplete>
)