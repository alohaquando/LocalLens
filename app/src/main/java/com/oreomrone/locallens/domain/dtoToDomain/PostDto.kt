package com.oreomrone.locallens.domain.dtoToDomain

import com.oreomrone.locallens.data.dto.PostDto
import com.oreomrone.locallens.domain.Place
import com.oreomrone.locallens.domain.Post
import com.oreomrone.locallens.domain.User

fun PostDto.toPost(): Post {
  return Post(
    id = id,
    place = placeDto.toPlace(),
    image = image,
    caption = caption,
    timestamp = timestamp,
    favorites = postsFavorites.map { it.toUser() },
    user = userDto.toUser(),
  )
}