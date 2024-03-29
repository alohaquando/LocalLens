package com.oreomrone.locallens.data.repositories.place

import com.oreomrone.locallens.data.dto.PlaceDto

interface PlaceRepository {
  suspend fun getPlace(id: String): PlaceDto?

  suspend fun createPlace(
    name: String,
    address: String,
    latitude: Double,
    longitude: Double
  ): Pair<Boolean, String>

  suspend fun getPlaceByAddress(address: String): PlaceDto?

  suspend fun getOrCreatePlace(
    name: String,
    address: String,
    latitude: Double,
    longitude: Double
  ): Pair<Boolean, String>

  suspend fun getPlacesByName(name: String): List<PlaceDto>
}

