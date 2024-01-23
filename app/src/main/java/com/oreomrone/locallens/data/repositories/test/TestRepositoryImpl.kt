package com.oreomrone.locallens.data.repositories.test

import com.oreomrone.locallens.data.repositories.place.PlaceRepository
import com.oreomrone.locallens.data.repositories.post.PostRepository
import com.oreomrone.locallens.data.repositories.profile.ProfileRepository
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import javax.inject.Inject

class TestRepositoryImpl @Inject constructor(
  private val auth: Auth,
  private val postgrest: Postgrest,
  private val storage: Storage,
  private val placeRepository: PlaceRepository,
  private val postRepository: PostRepository,
  private val profileRepository: ProfileRepository
) : TestRepository {
  override suspend fun test() {
      profileRepository.toggleFollow("67b4b73d-d8fe-4449-9a0b-fed55f12cfdc")
  }
}