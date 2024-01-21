package com.oreomrone.locallens.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.compose.auth.ComposeAuth
import io.github.jan.supabase.compose.auth.composeAuth
import io.github.jan.supabase.compose.auth.googleNativeLogin
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.ExternalAuthAction
import io.github.jan.supabase.gotrue.FlowType
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.serializer.KotlinXSerializer
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object SupabaseModule {
  @Provides
  @Singleton
  fun provideSupabaseClient(): SupabaseClient {
    return createSupabaseClient(
      supabaseUrl = "https://hyjllqwtvlxqtoxcgbkf.supabase.co",
      supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imh5amxscXd0dmx4cXRveGNnYmtmIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDU4MzYyMTcsImV4cCI6MjAyMTQxMjIxN30.qA90joK-cdTKegXhD-nWOMtVDqOGlAPPpjH4iREPMA0"
      ,
    ) {
      defaultSerializer = KotlinXSerializer(Json {
        ignoreUnknownKeys = true
      })
      install(Postgrest)
      install(Auth) {
        flowType = FlowType.PKCE
        scheme = "app"
        host = "com.oreomrone.locallens"
        defaultExternalAuthAction = ExternalAuthAction.CUSTOM_TABS
      }
      install(ComposeAuth) {
        googleNativeLogin(serverClientId = "728625137453-1drtdvg7del57kt3bajjubsn0c7jj8cc.apps.googleusercontent.com")
      }
      install(Storage)
    }
  }

  @Provides
  @Singleton
  fun provideSupabaseDatabase(client: SupabaseClient): Postgrest {
    return client.postgrest
  }

  @Provides
  @Singleton
  fun provideSupabaseAuth(client: SupabaseClient): Auth {
    return client.auth
  }

  @Provides
  @Singleton
  fun provideSupabaseAuthCompose(client: SupabaseClient): ComposeAuth {
    return client.composeAuth
  }

  @Provides
  @Singleton
  fun provideSupabaseStorage(client: SupabaseClient): Storage {
    return client.storage
  }

}