package com.oreomrone.locallens.data.repositories.profile

import android.util.Log
import com.oreomrone.locallens.data.dto.ProfileDto
import com.oreomrone.locallens.data.dto.ProfilesWrapperDto
import com.oreomrone.locallens.data.utils.BuildProfileImageUrl
import com.oreomrone.locallens.data.utils.cleanQueryString
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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
      val res = postgrest.from(table).select(
        Columns.raw(
          """
            *,
            followers: follows!follows_followed_fkey(follower),
            followings: follows!follows_follower_fkey(followed),
            places: posts!posts_owner_fkey(place(id))
          """.cleanQueryString()
        )
      ) {
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

      //region Upload image to storage and get URL
      var imageBucketUrl: String? = null

      if (imageFile != null && imageFile.isNotEmpty()) {
        val imageFileName = "${id}.png"
        imageBucketUrl = async {
          storage["profiles"].upload(
            path = imageFileName,
            data = imageFile,
            upsert = true
          )
        }.await()
        imageBucketUrl = BuildProfileImageUrl("${table}/${imageFileName}")
      }
      //endregion

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
          imageBucketUrl ?: imageUrl ?: ""
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

  override suspend fun validateUsernameUnique(username: String): Boolean {
    return try {
      if (username.isBlank()) return false
      if (username.isEmpty()) return false
      if (username.contains(" ")) return false

      val res = postgrest.from(table).select {
        filter {
          eq(
            "username",
            username
          )
        }
      }.decodeSingleOrNull<ProfileDto>()

      if (res !== null) {
        Log.e(
          "ProfileRepositoryImpl",
          "validateUsernameUnique:${res.id == auth.currentSessionOrNull()?.user?.id}"
        )
        auth.currentSessionOrNull()?.user?.id == res.id
      } else {
        true
      }
    } catch (e: RestException) {
      Log.e(
        "ProfileRepositoryImpl",
        "validateUsernameUnique: $e"
      )
      false
    }
  }


  override suspend fun getFollowersById(id: String): List<ProfileDto> {
    return try {
      val res = postgrest.from("follows").select(
        Columns.raw(
          """
            profiles!follows_follower_fkey(*)
          """.cleanQueryString()
        )
      ) {
        filter {
          eq(
            "followed",
            id
          )
        }
      }
      var followers = listOf<ProfileDto>()
      res.decodeList<ProfilesWrapperDto>().map { followers = followers.plus(it.profiles) }

      Log.d(
        "ProfileRepositoryImpl",
        "getFollowersById: $followers"
      )
      followers
    } catch (e: RestException) {
      Log.e(
        "ProfileRepositoryImpl",
        id
      )
      emptyList()
    }
  }

  override suspend fun getFollowingsById(id: String): List<ProfileDto> {
    return try {
      val res = postgrest.from("follows").select(
        Columns.raw(
          """
            profiles!follows_followed_fkey(*)
          """.cleanQueryString()
        )
      ) {
        filter {
          eq(
            "follower",
            id
          )
        }
      }
      var followings = listOf<ProfileDto>()
      res.decodeList<ProfilesWrapperDto>().map { followings = followings.plus(it.profiles) }

      Log.d(
        "ProfileRepositoryImpl",
        "getFollowingsById: $followings"
      )
      followings
    } catch (e: RestException) {
      Log.e(
        "ProfileRepositoryImpl",
        "getFollowingsById: $e"
      )
      emptyList()
    }
  }
}