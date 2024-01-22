package com.oreomrone.locallens.data.repositories.test

import com.oreomrone.locallens.data.repositories.place.PlaceRepository
import com.oreomrone.locallens.data.repositories.post.PostRepository
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import javax.inject.Inject

class TestRepositoryImpl @Inject constructor(
  private val auth: Auth,
  private val postgrest: Postgrest,
  private val storage: Storage,
  private val placeRepository: PlaceRepository,
  private val postRepository: PostRepository
) : TestRepository {
  override suspend fun test() {
//    postRepository.createPost(
//      imageFile = null,
//      imageUrl = "https://www.thelog.com.vn/images/uploaded/Gallery/Space/Updated%2005.2023/The%20Log%20(9%20of%2025).jpg",
//      caption = "Fancy girl dinner with my besties!",
//      visibility = "PRIVATE",
//      placeName = "The Log Restaurant",
//      placeAddress = "Rooftop, Gem Center, 8 Nguyễn Bỉnh Khiêm, Đa Kao, Quận 1, Thành phố Hồ Chí Minh, Vietnam",
//      placeLatitude = 10.789937,
//      placeLongitude = 106.702344
//    )
  }
}