package com.oreomrone.locallens.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FollowerDto (
  @SerialName("follower") val follower: String
)

@Serializable
data class FollowingsDto (
  @SerialName("followed") val followed: String
)