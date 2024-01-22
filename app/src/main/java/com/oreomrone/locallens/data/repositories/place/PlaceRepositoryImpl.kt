package com.oreomrone.locallens.data.repositories.place

import android.util.Log
import com.oreomrone.locallens.data.dto.PlaceDto
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import javax.inject.Inject
import kotlin.math.ceil

class PlaceRepositoryImpl @Inject constructor(
  private val postgrest: Postgrest,
  private val storage: Storage
) : PlaceRepository {
  private val table = "places"

  override suspend fun getPlace(id: String): PlaceDto? {
    return try {
      val res = postgrest.from(table).select {
        filter {
          eq(
            "id",
            id
          )
        }
      }.decodeSingleOrNull<PlaceDto>()
      Log.d(
        "PlaceRepositoryImpl",
        "getProfileById: $res"
      )
      res
    } catch (e: RestException) {
      Log.e(
        "PlaceRepositoryImpl",
        "getProfileById: $e"
      )
      null
    }
  }

  override suspend fun createPlace(
    name: String,
    address: String,
    latitude: Double,
    longitude: Double
  ): Pair<Boolean, String> {
    return try {
      val res = postgrest.from(table).insert(
        PlaceDto(
          name = name,
          address = address,
          latitude = latitude,
          longitude = longitude
        )
      ) { select() }.decodeSingleOrNull<PlaceDto>()
      if (res !== null) {
        Log.d(
          "PlaceRepositoryImpl",
          "createPlace: $res"
        )
        Pair(
          true,
          res.id
        )
      } else {
        Pair(
          false,
          "An error occurred. Please try again."
        )
      }
    } catch (e: RestException) {
      Log.e(
        "PlaceRepositoryImpl",
        "createPlace: $e"
      )
      Pair(
        false,
        e.message ?: "An error occurred. Please try again."
      )
    } catch (e: Exception) {
      Log.e(
        "PlaceRepositoryImpl",
        "createPlace: $e"
      )
      Pair(
        false,
        e.message ?: "An error occurred. Please try again."
      )
    }
  }

  override suspend fun getPlaceByAddress(address: String): PlaceDto? {
    return try {
      val res = postgrest.from(table).select {
        filter {
          eq(
            "address",
            address
          )
        }
      }.decodeSingleOrNull<PlaceDto>()
      Log.d(
        "PlaceRepositoryImpl",
        "getPlaceByAddress: $res"
      )
      res
    } catch (e: RestException) {
      Log.e(
        "PlaceRepositoryImpl",
        "getPlaceByAddress: $e"
      )
      null
    }
  }

  override suspend fun getOrCreatePlace(
    name: String,
    address: String,
    latitude: Double,
    longitude: Double
  ): Pair<Boolean, String> {
    return try {
      val existingPlace = getPlaceByAddress(address)

      Log.d(
        "PlaceRepositoryImpl",
        " $existingPlace")

      if (existingPlace != null) {
        Log.d(
          "PlaceRepositoryImpl",
          "getOrCreatePlace: Found existing place $existingPlace"
        )
        Pair(
          true,
          existingPlace.id
        )
      } else {
        val createdPlaceRes = createPlace(
          name = name,
          address = address,
          latitude = latitude,
          longitude = longitude)
        Log.d(
          "PlaceRepositoryImpl",
          "getOrCreatePlace: Created new place ${createdPlaceRes.second}"
        )
        createdPlaceRes
      }
    } catch (e: RestException) {
      Log.e(
        "PlaceRepositoryImpl",
        "getOrCreatePlace: $e"
      )
      Pair(
        false,
        e.message ?: "An error occurred. Please try again."
      )
    } catch (e: Exception) {
      Log.e(
        "PlaceRepositoryImpl",
        "getOrCreatePlace: $e"
      )
      Pair(
        false,
        e.message ?: "An error occurred. Please try again."
      )
    }
  }
}