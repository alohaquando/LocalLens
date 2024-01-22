package com.oreomrone.locallens.domain.dtoToDomain

import com.oreomrone.locallens.data.dto.ProfileDto
import com.oreomrone.locallens.domain.User

fun ProfileDto.toUser(): User {
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
    places = placesDtos.map { it.toPlace() }
  )
}
