package com.oreomrone.locallens.domain.dtoToDomain

import com.oreomrone.locallens.data.dto.PlaceAutocompleteDto
import com.oreomrone.locallens.domain.Place

fun PlaceAutocompleteDto.asDomainModel(): Place {
  return Place(
    id = id,
    name = displayNameDto.text,
    address = formattedAddress,
    latitude = locationDto.latitude,
    longitude = locationDto.longitude
  )
}