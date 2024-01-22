package com.oreomrone.locallens.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostFavoriteDto(
  @SerialName("post") val postId: String = "",
  @SerialName("favorited_by") val userId: String = ""
)