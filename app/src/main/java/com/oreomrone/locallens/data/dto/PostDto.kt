package com.oreomrone.locallens.data.dto

import com.oreomrone.locallens.domain.PostVisibilities
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class PostDto(
  @SerialName("id") val id: String = "",
  @SerialName("place") val placeId: String = "",
  @SerialName("places") val placeDto: PlaceDto,
  @SerialName("caption") val caption: String = "",
  @SerialName("timestamp") val timestamp: String = "",
  @SerialName("image") val image: String = "",
  @SerialName("profiles") val userDto: ProfileDto,
  @SerialName("owner") val userId: String = "",
  @SerialName("visibility") val visibility: String = PostVisibilities.PUBLIC.name,
  @SerialName("promoted_until") val promotedUntil: String = "",
  @SerialName("posts_favorites") val postsFavorites: List<PostFavoriteDto> = emptyList(),
)



