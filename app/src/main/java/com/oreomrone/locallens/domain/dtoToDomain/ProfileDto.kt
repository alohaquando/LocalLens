package com.oreomrone.locallens.domain.dtoToDomain

import com.oreomrone.locallens.data.dto.ProfileDto
import com.oreomrone.locallens.domain.Place
import com.oreomrone.locallens.domain.User

fun ProfileDto.toUser(): User {
  var places = emptySet<Place>()

  for (placeDto in placesWrapperDtos) {
    places = places.plus(placeDto.place.toPlace())
  }

  return User(
    id = id,
    name = fullName,
    username = username,
    email = email,
    bio = bio,
    image = image,
    isSuperUser = isSuperUser,
    isPrivate = isPrivate,
    followers = followersDtos.map { it.toUser() },
    followings = followingsDtos.map { it.toUser() },
    places = places.toList()
  )
}
