package com.oreomrone.locallens.data.repositories.post

import android.util.Log
import com.oreomrone.locallens.data.dto.PostDto
import com.oreomrone.locallens.data.repositories.place.PlaceRepository
import com.oreomrone.locallens.data.utils.BuildProfileImageUrl
import com.oreomrone.locallens.data.utils.cleanQueryString
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Count
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
  private val postgrest: Postgrest,
  private val storage: Storage,
  private val placeRepository: PlaceRepository
) : PostRepository {
  private val table = "posts"
  private val postQuery = """*,
    places(*),
    profiles!posts_owner_fkey(*),
    posts_favorites!posts_favorites_post_fkey(favorited_by)""".cleanQueryString()

  override suspend fun getAllPost(): List<PostDto> {
    return try {
      val res = postgrest.from(table).select(
        Columns.raw(
          postQuery
        )
      ).decodeList<PostDto>()
      Log.d(
        "PostRepositoryImpl",
        "getAllPost: $res"
      )
      res.sortedByDescending { it.timestamp }
    } catch (e: RestException) {
      Log.e(
        "PostRepositoryImpl",
        "getAllPost: $e"
      )
      emptyList()
    }
  }

  override suspend fun getPost(id: String): PostDto? {
    TODO("Not yet implemented")
  }

  override suspend fun createPost(
    imageFile: ByteArray?,
    imageUrl: String?,
    caption: String,
    visibility: String,
    placeName: String,
    placeAddress: String,
    placeLatitude: Double,
    placeLongitude: Double
  ): Pair<Boolean, String> = withContext(Dispatchers.IO) {
    return@withContext try {

      val placeRes = placeRepository.getOrCreatePlace(
        name = placeName,
        address = placeAddress,
        latitude = placeLatitude,
        longitude = placeLongitude
      )

      if (placeRes.first.not()) {
        Pair(
          false,
          placeRes.second
        )
      } else {

        //region Upload image to storage and get URL
        var imageBucketUrl: String? = null

        if (imageFile != null && imageFile.isNotEmpty()) {
          val imageFileName = "${UUID.randomUUID()}.png"
          imageBucketUrl = async {
            storage[table].upload(
              path = imageFileName,
              data = imageFile,
              upsert = true
            )
          }.await()
          imageBucketUrl = BuildProfileImageUrl("${table}/${imageFileName}")
        }
        //endregion

        val res = postgrest.from(table).insert(
          PostDto(
            caption = caption,
            image = imageBucketUrl ?: imageUrl ?: "",
            placeId = placeRes.second,
            visibility = visibility,
          )
        ) {
          select()
        }.decodeSingleOrNull<PostDto>()

        if (res != null) {
          Log.d(
            "PostRepositoryImpl",
            "createPost: $res"
          )
          Pair(
            true,
            "Post created successfully."
          )
        } else {
          Log.e(
            "PostRepositoryImpl",
            "createPost: Created but returned null $res"
          )
          Pair(
            false,
            "An error occurred. Please try again."
          )
        }
      }
    } catch (e: RestException) {
      Log.e(
        "PostRepositoryImpl",
        "createPost: $e"
      )
      Pair(
        false,
        e.message ?: "An error occurred. Please try again later."
      )
    } catch (e: Exception) {
      Log.e(
        "PostRepositoryImpl",
        "createPost: $e"
      )
      Pair(
        false,
        e.message ?: "An error occurred. Please try again later."
      )
    }
  }

  override suspend fun getPostsByPlaceId(placeId: String): List<PostDto> {
    return try {
      val res = postgrest.from(table).select(
        Columns.raw(
          postQuery
        )
      ) {
        filter {
          eq(
            "place",
            placeId
          )
        }
      }.decodeList<PostDto>()
      Log.d(
        "PostRepositoryImpl",
        "getPostsByPlaceId: $res"
      )
      res.sortedByDescending { it.timestamp }
    } catch (e: RestException) {
      Log.e(
        "PostRepositoryImpl",
        "getPostsByPlaceId: $e"
      )
      emptyList()
    }
  }

  override suspend fun getPostsByUserId(userId: String): List<PostDto> {
    return try {
      val res = postgrest.from(table).select(
        Columns.raw(
          postQuery
        )
      ) {
        filter {
          eq(
            "owner",
            userId
          )
        }
      }.decodeList<PostDto>()
      Log.d(
        "PostRepositoryImpl",
        "getPostsByUserId: $res"
      )
      res.sortedByDescending { it.timestamp }
    } catch (e: RestException) {
      Log.e(
        "PostRepositoryImpl",
        "getPostsByUserId: $e"
      )
      emptyList()
    }
  }

  override suspend fun getFavsCount(postId: String): Int {
    return try {
      val res = postgrest.from("posts_favorites").select {
        filter {
          eq(
            "post",
            postId
          )
        }
        count(Count.EXACT)
      }.countOrNull()!!

      Log.d(
        "PostRepositoryImpl",
        "getFavsCount: $res"
      )
      0
    } catch (e: RestException) {
      Log.e(
        "PostRepositoryImpl",
        "getFavsCount: $e"
      )
      0
    }
  }
}