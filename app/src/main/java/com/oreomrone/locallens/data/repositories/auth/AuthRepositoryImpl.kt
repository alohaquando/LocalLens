package com.oreomrone.locallens.data.repositories.auth

import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.builtin.Email
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
  private val auth: Auth
) : AuthRepository {
  override suspend fun signIn(
    email: String,
    password: String
  ): Pair<Boolean, String> {
    return try {
      auth.signInWith(Email) {
        this.email = email
        this.password = password
      }
      Pair(
        true,
        "Signed in as $email successfully. Please wait a moment..."
      )
    } catch (e: RestException) {
      when (e.message?.contains("invalid_grant")) {
        true -> {
          Pair(
            false,
            "The email or password doesn't match our records. Please try again."
          )
        }

        else -> {
          Pair(
            false,
            e.message ?: "An error occurred. Please try again."
          )
        }
      }
    }
  }

  override suspend fun signUp(
    email: String,
    password: String
  ): Pair<Boolean, String> {
    return try {
      auth.signUpWith(Email) {
        this.email = email
        this.password = password
      }
      Pair(
        true,
        "Sign up successful. Please check your email for the confirmation link."
      )
    } catch (e: RestException) {
      when (e.message?.contains("User already registered")) {
        true -> {
          Pair(
            false,
            "The email address is already registered."
          )
        }

        else -> {
          Pair(
            false,
            "An error occurred. Please try again.\nError message: ${e.message}"
          )
        }
      }
    }
  }

  override suspend fun signInWithGoogle(): Boolean {
    return try {
      auth.signInWith(Google)
      true
    } catch (e: Exception) {
      false
    }
  }

  override suspend fun signOut(): Boolean {
    return try {
      auth.signOut()
      true
    } catch (e: Exception) {
      false
    }
  }
}