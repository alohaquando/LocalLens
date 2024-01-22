package com.oreomrone.locallens.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceDto (
  @SerialName("id") val id: String = "",
  @SerialName("name") val name: String = "",
  @SerialName("address") val address: String = "",
  @SerialName("image") val image: String = "",
  @SerialName("latitude") val latitude: Double = 0.0,
  @SerialName("longitude") val longitude: Double = 0.0
)

@Serializable
data class PlacesWrapperDto (
  @SerialName("place") val place: PlaceDto = PlaceDto()
)