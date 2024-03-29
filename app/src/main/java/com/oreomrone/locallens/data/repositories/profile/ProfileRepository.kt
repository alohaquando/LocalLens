package com.oreomrone.locallens.data.repositories.profile

import com.oreomrone.locallens.data.dto.ProfileDto

interface ProfileRepository {
  suspend fun getAllProfile(): List<ProfileDto>
  suspend fun getProfileById(id: String): ProfileDto?
  suspend fun updateProfile(
    id: String,
    username: String,
    fullName: String,
    bio: String,
    isPrivate: Boolean,
    imageFile: ByteArray? = null,
    imageUrl: String? = null,
  ): Pair<Boolean, String>

  suspend fun validateUsernameUnique(username: String): Boolean

  suspend fun getFollowersById(id: String): List<ProfileDto>

  suspend fun getFollowingsById(id: String): List<ProfileDto>
  suspend fun getIsSuperUserById(id: String): Boolean
  suspend fun followProfile(id: String): Pair<Boolean, String>
  suspend fun unfollowProfile(id: String): Pair<Boolean, String>
  suspend fun toggleFollow(id: String): Pair<Boolean, String>
  suspend fun getProfilesByUsername(username: String): List<ProfileDto>
}

