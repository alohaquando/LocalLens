package com.oreomrone.locallens.data.repositories.test

import com.oreomrone.locallens.data.repositories.place.PlaceRepository
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import javax.inject.Inject

class TestRepositoryImpl @Inject constructor(
  private val auth: Auth,
  private val postgrest: Postgrest,
  private val storage: Storage,
  private val placeRepository: PlaceRepository
) : TestRepository {
  override suspend fun test() {
    placeRepository.getOrCreatePlace(
      name = "Frolic Bar",
      address = "Q1, 151 Đ. Đề Thám, Phường Cô Giang, Quận 1, Thành phố Hồ Chí Minh 00000, Vietnam",
      latitude = 10.765661300000001,
      longitude = 106.6945249,
    )
  }
}