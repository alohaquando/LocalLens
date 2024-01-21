package com.oreomrone.locallens.data.repositories.profile

import android.util.Log
import com.oreomrone.locallens.data.dto.ProfileDto
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
  private val auth: Auth,
  private val postgrest: Postgrest,
  private val storage: Storage
) : ProfileRepository {

  override suspend fun getAllProfile(): List<ProfileDto> {
    return try {
      val res = postgrest.from("profiles").select().decodeList<ProfileDto>()
      Log.d(
        "ProfileRepositoryImpl",
        "getAllProfile: $res"
      )
      res
    } catch (e: RestException) {
      Log.e(
        "ProfileRepositoryImpl",
        "getProfileById: $e"
      )
      emptyList()
    }
  }

  override suspend fun getProfileById(id: String): ProfileDto? {
    return try {
      val res = postgrest.from("profiles").select {
        filter {
          eq(
            "id",
            id
          )
        }
      }.decodeSingle<ProfileDto>()
      Log.d(
        "ProfileRepositoryImpl",
        "getProfileById: $res"
      )
      res
    } catch (e: RestException) {
      Log.e(
        "ProfileRepositoryImpl",
        "getProfileById: $e"
      )
      null
    }
  }
}