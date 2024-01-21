package com.oreomrone.locallens.data.repositories.profile

import com.oreomrone.locallens.data.dto.ProfileDto

interface ProfileRepository {
    suspend fun getAllProfile(): List<ProfileDto>
    suspend fun getProfileById(id: String): ProfileDto?
}

