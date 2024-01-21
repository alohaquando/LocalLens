package com.oreomrone.locallens.domain.dtoToDomain

import com.oreomrone.locallens.data.dto.PlaceAutocomplete
import com.oreomrone.locallens.domain.Place

fun PlaceAutocomplete.asDomainModel(): Place {
  return Place(
    id = id,
    name = displayName.text,
    address = formattedAddress,
    latitude = location.latitude,
    longitude = location.longitude
  )
}