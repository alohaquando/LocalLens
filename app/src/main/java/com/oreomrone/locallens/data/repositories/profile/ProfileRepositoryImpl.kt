package com.oreomrone.locallens.data.repositories.profile

import android.util.Log
import com.oreomrone.locallens.data.dto.ProfileDto
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
  private val auth: Auth,
  private val postgrest: Postgrest,
  private val storage: Storage
) : ProfileRepository {
  private val table = "profiles"

  override suspend fun getAllProfile(): List<ProfileDto> {
    return try {
      val res = postgrest.from(table).select().decodeList<ProfileDto>()
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
      val res = postgrest.from(table).select {
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

  override suspend fun updateProfile(
    id: String,
    username: String,
    fullName: String,
    bio: String,
    isPrivate: Boolean,
    imageFile: ByteArray?,
    imageUrl: String?
  ): Pair<Boolean, String> = withContext(Dispatchers.IO) {
    return@withContext try {
      var imageBucketUrl: String? = null

      if (imageFile != null) {
        val imageFileName = "${id}.png"
        imageBucketUrl = storage["profile_images"].upload(
          path = imageFileName,
          data = imageFile,
          upsert = true
        )
      }

      postgrest.from(table).update({
        set(
          "username",
          username
        )
        set(
          "full_name",
          fullName
        )
        set(
          "bio",
          bio
        )
        set(
          "is_private",
          isPrivate
        )
        set(
          "avatar_url",
          imageBucketUrl ?: imageUrl?: ""
        )
      }) {
        filter {
          eq(
            "id",
            id
          )
        }
      }
      Pair(
        true,
        "Updated successfully"
      )
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