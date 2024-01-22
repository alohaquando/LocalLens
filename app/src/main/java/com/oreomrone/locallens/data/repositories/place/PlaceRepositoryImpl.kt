package com.oreomrone.locallens.data.repositories.place

import android.util.Log
import com.oreomrone.locallens.data.dto.PlaceDto
import com.oreomrone.locallens.data.dto.ProfileDto
import com.oreomrone.locallens.data.utils.BuildProfileImageUrl
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.util.Locale.filter
import javax.inject.Inject

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
      }.decodeSingle<PlaceDto>()
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
      ).decodeSingleOrNull<PlaceDto>()
      if (res!== null) {
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
        "ProfileRepositoryImpl",
        "updateProfile: $e"
      )
      Pair(
        false,
        e.message ?: "An error occurred. Please try again."
      )
    }
  }
}