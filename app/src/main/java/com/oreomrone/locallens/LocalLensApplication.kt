package com.oreomrone.locallens

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LocalLensApplication : Application() {
  override fun onCreate() {
    super.onCreate()

    // Messaging notification channel
    val messagingNotificationChannel = NotificationChannel(
      "messaging_notification_channel",
      "Messaging notifications",
      NotificationManager.IMPORTANCE_HIGH
    )
    messagingNotificationChannel.description = "For messaging-related notifications"

    // Post notification channel
    val postNotificationChannel = NotificationChannel(
      "post_notification_channel",
      "Posts notifications",
      NotificationManager.IMPORTANCE_DEFAULT
    )
    postNotificationChannel.description = "For post-related notifications"

    // Profile notification channel
    val profileNotificationChannel = NotificationChannel(
      "profile_notification_channel",
      "Profile notifications",
      NotificationManager.IMPORTANCE_DEFAULT
    )
    profileNotificationChannel.description = "For profile-related notifications"

    // Register the channel with the system
    val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannels(
      listOf(
        messagingNotificationChannel,
        postNotificationChannel,
        profileNotificationChannel
      )
    )
  }
}