package com.oreomrone.locallens.domain.dtoToDomain

import com.oreomrone.locallens.data.dto.FollowerDto
import com.oreomrone.locallens.data.dto.FollowingsDto
import com.oreomrone.locallens.domain.User

fun FollowerDto.toUser(): User {
  return User(
    id = follower,
  )
}

fun FollowingsDto.toUser(): User {
  return User(
    id = followed,
  )
}