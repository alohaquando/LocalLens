package com.oreomrone.locallens.data.repositories.test

import android.util.Log
import com.oreomrone.locallens.data.dto.ProfileDto
import com.oreomrone.locallens.data.utils.cleanQueryString
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.Storage
import javax.inject.Inject

class TestRepositoryImpl @Inject constructor(
  private val auth: Auth,
  private val postgrest: Postgrest,
  private val storage: Storage
) : TestRepository {
  override suspend fun test() {
    try {
      val res = postgrest.from("profiles").select(
        Columns.raw(
          """
            *,
            followers: follows!follows_followed_fkey(follower),
            followings: follows!follows_follower_fkey(followed),
            places: posts!posts_owner_fkey(place)
          """.cleanQueryString()
        )
      ){
        filter{
          eq(
            "id",
            "8b0c4a1d-3633-4f3b-a336-dc4bf69f88f9"
          )
        }
        single()
      }
      Log.d(
        "TestRepository",
        "Test: ${res.data}"
      )
    } catch (e: RestException) {
      Log.e(
        "TestRepository",
        "Test: $e"
      )
    }
  }
}