@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  alias(libs.plugins.androidApplication)
  alias(libs.plugins.kotlinAndroid)
  alias(libs.plugins.ksp)
  alias(libs.plugins.daggerHilt)
  alias(libs.plugins.kotlinSerialization)
}

android {
  namespace = "com.oreomrone.locallens"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.oreomrone.locallens"
    minSdk = 26
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
      useSupportLibrary = true
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.7"
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
}

dependencies {

  implementation(libs.core.ktx)
  implementation(libs.lifecycle.runtime.ktx)
  implementation(libs.activity.compose)
  implementation(platform(libs.compose.bom))
  implementation(libs.ui)
  implementation(libs.ui.graphics)
  implementation(libs.ui.tooling.preview)
  implementation(libs.material3)
  implementation(libs.material)
  implementation(libs.coil.compose)
  implementation(libs.androidx.material.icons.extended)
  implementation(libs.androidx.navigation.compose)
  implementation(libs.maps.compose)
  implementation(libs.maps.compose.utils)
  implementation(libs.hilt.android)
  implementation(libs.androidx.hilt.navigation.compose)
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.accompanist.permissions)
  implementation(libs.coil.compose)
  implementation(libs.ktor.client.core)
  implementation(libs.ktor.utils)
  implementation(libs.ktor.client.cio)
  implementation(libs.ktor.client.android)
  implementation(libs.ktor.client.json)
  implementation(libs.ktor.client.serialization)
  implementation(libs.ktor.client.logging)
  implementation(libs.ktor.client.content.negotiation)
  implementation(libs.ktor.serialization.kotlinx.json)
  implementation(platform(libs.supabase.bom))
  implementation(libs.supabase.compose.auth)
  implementation(libs.supabase.coil.integration)
  implementation(libs.supabase.postgrest.kt)
  implementation(libs.supabase.storage.kt)
  implementation(libs.supabase.gotrue.kt)
  implementation(libs.supabase.realtime.kt)
  ksp(libs.hilt.android.compiler)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.test.ext.junit)
  androidTestImplementation(libs.espresso.core)
  androidTestImplementation(platform(libs.compose.bom))
  androidTestImplementation(libs.ui.test.junit4)
  debugImplementation(libs.ui.tooling)
  debugImplementation(libs.ui.test.manifest)
}