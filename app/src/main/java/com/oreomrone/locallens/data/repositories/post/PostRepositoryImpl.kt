package com.oreomrone.locallens.data.repositories.post

import android.util.Log
import com.oreomrone.locallens.data.dto.PostDto
import com.oreomrone.locallens.data.utils.cleanQueryString
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.Storage
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
  private val postgrest: Postgrest,
  private val storage: Storage
) : PostRepository {
  private val table = "posts"

  override suspend fun getAllPost(): List<PostDto> {
    return try {
      val res = postgrest.from(table).select(
        Columns.raw(
          """*,places(*),profiles!posts_owner_fkey(*)""".cleanQueryString()
        )
      ).decodeList<PostDto>()
      Log.d(
        "PostRepositoryImpl",
        "getAllPost: $res"
      )
      res
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
}