package com.oreomrone.locallens.data.repositories.test

import com.oreomrone.locallens.data.dto.ProfileDto

interface TestRepository {
  suspend fun test(): Unit
}

