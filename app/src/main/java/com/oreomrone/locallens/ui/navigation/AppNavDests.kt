package com.oreomrone.locallens.ui.navigation

enum class AppNavDests {
  // Account Settings
  AccountSettings,
  AccountSettingsChangeEmailNew,
//  AccountSettingsChangeEmailVerify,
//  AccountSettingsChangePasswordConfirm,
  AccountSettingsChangePasswordNew,
  AccountSettingsChangeProfile,
  ManageOtherAccounts,
  Support,

  // Auth
  AuthSignIn,
  AuthSignUp,
  AuthResetPasswordEmail,
  AuthResetPasswordCheckEmail,
  AuthResetPasswordNewPassword,

  // Complete Account
  CompleteAccountProfile,

  // Details
  DetailsPerson,
  DetailsPlace,
  DetailsFollowers,
  DetailsFollowing,
  DetailsPostsMap,

  // Discover
  Discover,
  DiscoverSearch,

  // Me
  Me,

  // Messages
  MessagesDetails,
  MessagesList,

  // Posts
  Posts,
  NewPostDetails,
  EditPostDetails,

  // Notifications
  Notifications,
}