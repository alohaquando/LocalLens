package com.oreomrone.locallens.domain.dtoToDomain

import com.oreomrone.locallens.data.dto.PlaceDto
import com.oreomrone.locallens.domain.Place

fun PlaceDto.toPlace() : Place {
  return Place(
    id = id,
    name = name,
    address = address,
    image = image,
    latitude = latitude,
    longitude = longitude
  )
}