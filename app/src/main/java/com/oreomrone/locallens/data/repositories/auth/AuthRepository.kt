package com.oreomrone.locallens.data.repositories.auth

interface AuthRepository {
    suspend fun signIn(email: String, password: String): Pair<Boolean, String>
    suspend fun signUp(email: String, password: String): Pair<Boolean, String>
    suspend fun signInWithGoogle(): Boolean
    suspend fun signOut(): Boolean
}

