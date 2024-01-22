package com.oreomrone.locallens.domain.dtoToDomain

import com.oreomrone.locallens.data.dto.PostDto
import com.oreomrone.locallens.domain.Place
import com.oreomrone.locallens.domain.Post
import com.oreomrone.locallens.domain.User

fun PostDto.toPost(place: Place, user: User): Post {
  return Post(
    id = id,
    place = place,
    image = image,
    caption = caption,
    timestamp = timestamp,
    favorites = listOf(),
    user = user,
  )
}