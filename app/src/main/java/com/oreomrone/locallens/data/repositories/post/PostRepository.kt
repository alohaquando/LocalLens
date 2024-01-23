package com.oreomrone.locallens.data.repositories.post

import com.oreomrone.locallens.data.dto.PlaceDto
import com.oreomrone.locallens.data.dto.PostDto

interface PostRepository {
 suspend fun getAllPost(): List<PostDto>

  suspend fun getPost(id: String): PostDto?

  suspend fun getPostsByPlaceId(placeId: String): List<PostDto>

  suspend fun getPostsByUserId(userId: String): List<PostDto>

  suspend fun getFavsCount(postId: String): Int
  suspend fun createPost(
    imageFile: ByteArray?,
    imageUrl: String?,
    caption: String,
    visibility: String,
    placeName: String,
    placeAddress: String,
    placeLatitude: Double,
    placeLongitude: Double
  ): Pair<Boolean, String>

  suspend fun favoritePost(id: String): Pair<Boolean, String>
  suspend fun unfavoritePost(id: String): Pair<Boolean, String>
  suspend fun toggleFavorite(id: String)
}

