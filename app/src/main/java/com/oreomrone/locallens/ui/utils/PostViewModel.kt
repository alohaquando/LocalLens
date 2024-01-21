package com.oreomrone.locallens.ui.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class PostViewModel @Inject constructor(

) : ViewModel() {
  suspend fun refreshPost() {
    // TODO
  }

  suspend fun performPromotePost(id: String) {
    // TODO
  }

  suspend fun performDeletePost(id: String) {
    // TODO
  }

  suspend fun performFavoritePost(id: String) {
    // TODO
  }

  fun performNavigate(
    lat: Double,
    long: Double,
    name: String,
    context: Context,
    packageManager: PackageManager
  ) {
    val gmmIntentUri = Uri.parse("geo:${lat},${long}?q=${lat},${long}(${name})")
    val mapIntent = Intent(
      Intent.ACTION_VIEW,
      gmmIntentUri
    )
    mapIntent.setPackage("com.google.android.apps.maps")
    mapIntent.resolveActivity(packageManager)?.let {
      ContextCompat.startActivity(
        context,
        mapIntent,
        null
      )
    }
  }
}