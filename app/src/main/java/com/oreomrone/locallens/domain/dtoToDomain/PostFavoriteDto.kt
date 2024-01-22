package com.oreomrone.locallens.domain.dtoToDomain

import com.oreomrone.locallens.data.dto.PostFavoriteDto
import com.oreomrone.locallens.domain.User

fun PostFavoriteDto.toUser(): User {
  return User(
    id = userId,
  )
}