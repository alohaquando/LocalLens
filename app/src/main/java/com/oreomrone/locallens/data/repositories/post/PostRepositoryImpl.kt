package com.oreomrone.locallens.data.repositories.post

import android.util.Log
import com.oreomrone.locallens.data.dto.PostDto
import com.oreomrone.locallens.data.utils.cleanQueryString
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Count
import io.github.jan.supabase.storage.Storage
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
  private val postgrest: Postgrest,
  private val storage: Storage
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
    placeName: String,
    placeAddress: String,
    placeLatitude: Double,
    placeLongitude: Double
  ): Pair<Boolean, String> {
    TODO("Not yet implemented")
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