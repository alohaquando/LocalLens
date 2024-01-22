package com.oreomrone.locallens.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
  @SerialName("id") val id: String = "",
  @SerialName("full_name") val fullName: String = "",
  @SerialName("username") val username: String = "",
  @SerialName("email") val email: String = "",
  @SerialName("bio") val bio: String = "",
  @SerialName("avatar_url") val image: String = "",
  @SerialName("is_super_user") val isSuperUser: Boolean = false,
  @SerialName("is_private") val isPrivate: Boolean = false,
)