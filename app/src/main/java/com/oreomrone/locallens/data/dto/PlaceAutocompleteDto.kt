package com.oreomrone.locallens.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationDto(
    val latitude: Double,
    val longitude: Double
)

@Serializable
data class DisplayNameDto(
    val text: String,
    @SerialName("languageCode")
    val languageCode: String? = null
)

@Serializable
data class PlaceAutocompleteDto(
    val id: String,
    @SerialName("formattedAddress")
    val formattedAddress: String,
    @SerialName("location")
    val locationDto: LocationDto,
    @SerialName("displayName")
    val displayNameDto: DisplayNameDto
)

@Serializable
data class PlacesResponseDto(
    @SerialName("places") val placesDto: List<PlaceAutocompleteDto>
)