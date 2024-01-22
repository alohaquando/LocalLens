package com.oreomrone.locallens.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostDto(
  @SerialName("id") val id: String = "",
  @SerialName("place") val placeId: String = "",
  @SerialName("caption") val caption: String = "",
  @SerialName("timestamp") val timestamp: String = "",
  @SerialName("image") val image: String = "",
  @SerialName("owner") val ownerId: String = "",
  @SerialName("visibility") val visibility: String = PostVisibilities.PUBLIC.name,
  @SerialName("promoted_until") val promotedUntil: String = "",
  )

enum class PostVisibilities {
  PUBLIC,
  PRIVATE,
  ME
}
