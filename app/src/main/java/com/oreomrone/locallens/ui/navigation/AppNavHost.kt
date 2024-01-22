package com.oreomrone.locallens.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.oreomrone.locallens.ui.screens.accountSettings.AccountSettings
import com.oreomrone.locallens.ui.screens.accountSettings.changeEmail.AccountSettingsChangeEmailNew
import com.oreomrone.locallens.ui.screens.accountSettings.changePassword.AccountSettingsChangePasswordNew
import com.oreomrone.locallens.ui.screens.accountSettings.changeProfile.AccountSettingsChangeProfile
import com.oreomrone.locallens.ui.screens.accountSettings.support.Support
import com.oreomrone.locallens.ui.screens.auth.AuthSignIn
import com.oreomrone.locallens.ui.screens.auth.AuthSignUp
import com.oreomrone.locallens.ui.screens.auth.resetPassword.AuthResetPasswordCheckEmail
import com.oreomrone.locallens.ui.screens.auth.resetPassword.AuthResetPasswordEmail
import com.oreomrone.locallens.ui.screens.auth.resetPassword.AuthResetPasswordNewPassword
import com.oreomrone.locallens.ui.screens.completeAccount.CompleteAccountProfile
import com.oreomrone.locallens.ui.screens.details.DetailsPerson
import com.oreomrone.locallens.ui.screens.details.DetailsPlace
import com.oreomrone.locallens.ui.screens.details.follows.DetailsFollowers
import com.oreomrone.locallens.ui.screens.details.follows.DetailsFollowing
import com.oreomrone.locallens.ui.screens.details.map.DetailsPostsMap
import com.oreomrone.locallens.ui.screens.discover.Discover
import com.oreomrone.locallens.ui.screens.discover.DiscoverSearch
import com.oreomrone.locallens.ui.screens.me.Me
import com.oreomrone.locallens.ui.screens.messages.MessagesDetails
import com.oreomrone.locallens.ui.screens.messages.MessagesList
import com.oreomrone.locallens.ui.screens.notifications.Notifications
import com.oreomrone.locallens.ui.screens.posts.Posts
import com.oreomrone.locallens.ui.screens.posts.editPost.EditPostDetails
import com.oreomrone.locallens.ui.screens.posts.newPost.NewPostDetails
import com.oreomrone.locallens.ui.utils.enterCutTransition
import com.oreomrone.locallens.ui.utils.enterFadeTransition
import com.oreomrone.locallens.ui.utils.enterHorizontalTransition
import com.oreomrone.locallens.ui.utils.enterVerticalTransition
import com.oreomrone.locallens.ui.utils.exitCutTransition
import com.oreomrone.locallens.ui.utils.exitFadeTransition
import com.oreomrone.locallens.ui.utils.exitHorizontalTransition
import com.oreomrone.locallens.ui.utils.exitVerticalTransition
import com.oreomrone.locallens.ui.utils.popEnterCutTransition
import com.oreomrone.locallens.ui.utils.popEnterFadeTransition
import com.oreomrone.locallens.ui.utils.popEnterHorizontalTransition
import com.oreomrone.locallens.ui.utils.popEnterVerticalTransition
import com.oreomrone.locallens.ui.utils.popExitCutTransition
import com.oreomrone.locallens.ui.utils.popExitFadeTransition
import com.oreomrone.locallens.ui.utils.popExitHorizontalTransition
import com.oreomrone.locallens.ui.utils.popExitVerticalTransition

@Composable
fun AppNavHost(
  modifier: Modifier = Modifier,
  navController: NavHostController = rememberNavController(),
  startDestination: String = AppNavDests.AuthSignIn.name,
) {
  NavHost(
    navController = navController,
    startDestination = startDestination,
    modifier = modifier
  ) {
    //region Account Settings
    composable(
      AppNavDests.AccountSettings.name,
      enterTransition = enterHorizontalTransition,
      exitTransition = exitHorizontalTransition,
      popEnterTransition = popEnterHorizontalTransition,
      popExitTransition = popExitHorizontalTransition,
    ) {
      AccountSettings(
        backOnClick = { navController.popBackStack() },
        changePasswordOnClick = {
          navController.navigate(
            AppNavDests.AccountSettingsChangePasswordNew.name
          )
        },
        changeEmailOnClick = {
          navController.navigate(
            AppNavDests.AccountSettingsChangeEmailNew.name
          )
        },
        supportOnClick = { navController.navigate(AppNavDests.Support.name) },
      )
    }

    //region Change Password
//    composable(AppNavDests.AccountSettingsChangePasswordConfirm.name) {
//      AccountSettingsChangePasswordConfirm()
//    }
    composable(
      AppNavDests.AccountSettingsChangePasswordNew.name,
      enterTransition = enterHorizontalTransition,
      exitTransition = exitHorizontalTransition,
      popEnterTransition = popEnterHorizontalTransition,
      popExitTransition = popExitHorizontalTransition,
    ) {
      AccountSettingsChangePasswordNew(
        navigationButtonOnClick = { navController.popBackStack() },
      )
    }
    //endregion

    //region Change Email
    composable(
      AppNavDests.AccountSettingsChangeEmailNew.name,
      enterTransition = enterHorizontalTransition,
      exitTransition = exitHorizontalTransition,
      popEnterTransition = popEnterHorizontalTransition,
      popExitTransition = popExitHorizontalTransition,
    ) {
      AccountSettingsChangeEmailNew(
        navigationButtonOnClick = { navController.popBackStack() },
      )
    }
//    composable(AppNavDests.AccountSettingsChangeEmailVerify.name) {
//      AccountSettingsChangeEmailVerify()
//    }
    //endregion

    composable(
      AppNavDests.AccountSettingsChangeProfile.name,
      enterTransition = enterHorizontalTransition,
      exitTransition = exitHorizontalTransition,
      popEnterTransition = popEnterHorizontalTransition,
      popExitTransition = popExitHorizontalTransition,
    ) {
      AccountSettingsChangeProfile(
        backOnClick = { navController.popBackStack() },
      )
    }

    composable(
      AppNavDests.Support.name,
      enterTransition = enterHorizontalTransition,
      exitTransition = exitHorizontalTransition,
      popEnterTransition = popEnterHorizontalTransition,
      popExitTransition = popExitHorizontalTransition,
    ) {
      Support(
        onBackClicked = { navController.popBackStack() },
      )
    }
    //endregion

    //region Authentication
    composable(
      AppNavDests.AuthSignIn.name,
      enterTransition = enterVerticalTransition,
      exitTransition = exitVerticalTransition,
      popEnterTransition = popEnterVerticalTransition,
      popExitTransition = popExitVerticalTransition
    ) {
      AuthSignIn(
        passwordResetOnClick = { navController.navigate(AppNavDests.AuthResetPasswordEmail.name) },
        signUpOnClick = {
          navController.popBackStack()
          navController.navigate(AppNavDests.AuthSignUp.name)
        },
        navigateToMe = {
          navController.popBackStack()
          navController.navigate(AppNavDests.Me.name)
        },
      )
    }

    composable(
      AppNavDests.AuthSignUp.name,
      enterTransition = enterVerticalTransition,
      exitTransition = exitVerticalTransition,
      popEnterTransition = popEnterVerticalTransition,
      popExitTransition = popExitVerticalTransition
    ) {
      AuthSignUp(
        signInOnClick = {
          navController.popBackStack()
          navController.navigate(AppNavDests.AuthSignIn.name)
        },
        navigateToCompleteProfile = {
          navController.popBackStack()
          navController.navigate(AppNavDests.CompleteAccountProfile.name)
        },
      )
    }
    //region Reset Password
    composable(
      AppNavDests.AuthResetPasswordEmail.name,
      enterTransition = enterVerticalTransition,
      exitTransition = exitVerticalTransition,
      popEnterTransition = popEnterVerticalTransition,
      popExitTransition = popExitVerticalTransition
    ) {
      AuthResetPasswordEmail(
        navigationButtonOnClick = { navController.navigate(AppNavDests.AuthSignIn.name) },
        navigateToCheckEmail = { navController.navigate(AppNavDests.AuthResetPasswordCheckEmail.name) },
      )
    }
    composable(
      AppNavDests.AuthResetPasswordCheckEmail.name,
      enterTransition = enterVerticalTransition,
      exitTransition = exitVerticalTransition,
      popEnterTransition = popEnterVerticalTransition,
      popExitTransition = popExitVerticalTransition
    ) {
      AuthResetPasswordCheckEmail(
        navigationButtonOnClick = { navController.navigate(AppNavDests.AuthSignIn.name) },
      )
    }
    composable(
      AppNavDests.AuthResetPasswordNewPassword.name,
      enterTransition = enterVerticalTransition,
      exitTransition = exitVerticalTransition,
      popEnterTransition = popEnterVerticalTransition,
      popExitTransition = popExitVerticalTransition
    ) {
      AuthResetPasswordNewPassword(
        navigationButtonOnClick = { navController.navigate(AppNavDests.AuthSignIn.name) },
      )
    }
    //endregion
    //endregion

    //region Complete Account
    composable(
      AppNavDests.CompleteAccountProfile.name,
      enterTransition = enterVerticalTransition,
      exitTransition = exitVerticalTransition,
      popEnterTransition = popEnterVerticalTransition,
      popExitTransition = popExitVerticalTransition
    ) {
      CompleteAccountProfile(navigateToMe = {
        navController.popBackStack()
        navController.navigate(AppNavDests.Me.name) })
    }
    //endregion

    //region Details
    composable(
      AppNavDests.DetailsPlace.name + "/{id}",
      enterTransition = enterHorizontalTransition,
      exitTransition = exitHorizontalTransition,
      popEnterTransition = popEnterHorizontalTransition,
      popExitTransition = popExitHorizontalTransition,
    ) {
      DetailsPlace(
        backOnClick = { navController.popBackStack() },
        userOnClick = { navController.navigate(AppNavDests.DetailsPerson.name + "/${it}") },
        editOnClick = { navController.navigate(AppNavDests.DetailsPerson.name + "/${it}") },
      )
    }
    composable(
      AppNavDests.DetailsPerson.name + "/{id}",
      enterTransition = enterHorizontalTransition,
      exitTransition = exitHorizontalTransition,
      popEnterTransition = popEnterHorizontalTransition,
      popExitTransition = popExitHorizontalTransition,
    ) {
      DetailsPerson(
        backOnClick = { navController.popBackStack() },
        placesOnClick = { navController.navigate(AppNavDests.DetailsPostsMap.name + "/${it}") },
        followersOnClick = { navController.navigate(AppNavDests.DetailsFollowers.name + "/${it}") },
        followingOnClick = { navController.navigate(AppNavDests.DetailsFollowing.name + "/${it}") },
        placeOnClick = { navController.navigate(AppNavDests.DetailsPlace.name + "/${it}") },
        userOnClick = { navController.navigate(AppNavDests.DetailsPerson.name + "/${it}") },
        editOnClick = { navController.navigate(AppNavDests.EditPostDetails.name + "/${it}") },
      )
    }
    composable(
      AppNavDests.DetailsFollowers.name + "/{id}",
      enterTransition = enterHorizontalTransition,
      exitTransition = exitHorizontalTransition,
      popEnterTransition = popEnterHorizontalTransition,
      popExitTransition = popExitHorizontalTransition,
    ) {
      DetailsFollowers(
        backOnClick = { navController.popBackStack() },
        userOnClick = { navController.navigate(AppNavDests.DetailsPerson.name + "/${it}") },
      )
    }
    composable(
      AppNavDests.DetailsFollowing.name + "/{id}",
      enterTransition = enterHorizontalTransition,
      exitTransition = exitHorizontalTransition,
      popEnterTransition = popEnterHorizontalTransition,
      popExitTransition = popExitHorizontalTransition,
    ) {
      DetailsFollowing(
        backOnClick = { navController.popBackStack() },
        userOnClick = { navController.navigate(AppNavDests.DetailsPerson.name + "/${it}") },
      )
    }
    composable(
      AppNavDests.DetailsPostsMap.name + "/{id}",
      enterTransition = enterHorizontalTransition,
      exitTransition = exitHorizontalTransition,
      popEnterTransition = popEnterHorizontalTransition,
      popExitTransition = popExitHorizontalTransition,
    ) {
      DetailsPostsMap(
        backOnClick = { navController.popBackStack() },
        placeOnClick = { navController.navigate(AppNavDests.DetailsPlace.name + "/${it}") },
        userOnClick = { navController.navigate(AppNavDests.DetailsPerson.name + "/${it}") },
        editOnClick = { navController.navigate(AppNavDests.EditPostDetails.name + "/${it}") },
      )
    }
    //endregion

    //region Discover
    composable(
      AppNavDests.Discover.name,
      enterTransition = enterCutTransition,
      exitTransition = exitCutTransition,
      popEnterTransition = popEnterCutTransition,
      popExitTransition = popExitCutTransition
    ) {
      Discover(
        searchBarOnClick = { navController.navigate(AppNavDests.DiscoverSearch.name) },
        placeOnClick = { navController.navigate(AppNavDests.DetailsPlace.name + "/${it}") },
        userOnClick = { navController.navigate(AppNavDests.DetailsPerson.name + "/${it}") },
        editOnClick = { navController.navigate(AppNavDests.EditPostDetails.name + "/${it}") },
      )
    }
    composable(
      AppNavDests.DiscoverSearch.name,
      enterTransition = enterFadeTransition,
      exitTransition = exitFadeTransition,
      popEnterTransition = popEnterFadeTransition,
      popExitTransition = popExitFadeTransition
    ) {
      DiscoverSearch(
        backOnClick = { navController.popBackStack() },
        placeResultOnClick = { navController.navigate(AppNavDests.DetailsPlace.name + "/${it}") },
        userResultOnClick = { navController.navigate(AppNavDests.DetailsPerson.name + "/${it}") },
      )
    }
    //endregion

    //region Me
    composable(
      AppNavDests.Me.name,
      enterTransition = enterCutTransition,
      exitTransition = exitCutTransition,
      popEnterTransition = popEnterCutTransition,
      popExitTransition = popExitCutTransition
    ) {
      Me(
        editProfileOnClick = { navController.navigate(AppNavDests.AccountSettingsChangeProfile.name) },
        accountSettingsOnClick = { navController.navigate(AppNavDests.AccountSettings.name) },
        placeOnClick = { navController.navigate(AppNavDests.DetailsPlace.name + "/${it}") },
        userOnClick = { navController.navigate(AppNavDests.DetailsPerson.name + "/${it}") },
        editOnClick = { navController.navigate(AppNavDests.EditPostDetails.name + "/${it}") },
        placesOnClick = { navController.navigate(AppNavDests.DetailsPostsMap.name + "/${it}") },
        followersOnClick = { navController.navigate(AppNavDests.DetailsFollowers.name + "/${it}") },
        followingOnClick = { navController.navigate(AppNavDests.DetailsFollowing.name + "/${it}") },
      )
    }
    //endregion

    //region Messages
    composable(
      AppNavDests.MessagesList.name,
      enterTransition = enterCutTransition,
      exitTransition = exitCutTransition,
      popEnterTransition = popEnterCutTransition,
      popExitTransition = popExitCutTransition
    ) {
      MessagesList(
        threadOnClick = { navController.navigate(AppNavDests.MessagesDetails.name + "/${it}") },
      )
    }
    composable(
      AppNavDests.MessagesDetails.name + "/{id}",
      enterTransition = enterHorizontalTransition,
      exitTransition = exitHorizontalTransition,
      popEnterTransition = popEnterHorizontalTransition,
      popExitTransition = popExitHorizontalTransition,
    ) {
      MessagesDetails(
        backOnClick = { navController.popBackStack() },
      )
    }
    //endregion

    //region Notifications
    composable(
      AppNavDests.Notifications.name,
      enterTransition = enterVerticalTransition,
      exitTransition = exitVerticalTransition,
      popEnterTransition = popEnterVerticalTransition,
      popExitTransition = popExitVerticalTransition
    ) {
      Notifications(
        backOnClick = { navController.popBackStack() },
        notificationOnClick = { navController.navigate(AppNavDests.DetailsPerson.name + "/${it}") },
      )
    }
    //endregion

    //region Posts
    composable(
      AppNavDests.Posts.name,
      enterTransition = enterCutTransition,
      exitTransition = exitCutTransition,
      popEnterTransition = popEnterCutTransition,
      popExitTransition = popExitCutTransition
    ) {
      Posts(
        notificationsOnClick = { navController.navigate(AppNavDests.Notifications.name) },
        placeOnClick = { navController.navigate(AppNavDests.DetailsPlace.name + "/${it}") },
        userOnClick = { navController.navigate(AppNavDests.DetailsPerson.name + "/${it}") },
        editOnClick = { navController.navigate(AppNavDests.EditPostDetails.name + "/${it}") },
      )
    }
    //region Post Details
    composable(
      AppNavDests.NewPostDetails.name,
      enterTransition = enterVerticalTransition,
      exitTransition = exitVerticalTransition,
      popEnterTransition = popEnterVerticalTransition,
      popExitTransition = popExitVerticalTransition
    ) {
      NewPostDetails(backOnClick = { navController.popBackStack() })
    }
    composable(
      AppNavDests.EditPostDetails.name + "/{id}",
      enterTransition = enterVerticalTransition,
      exitTransition = exitVerticalTransition,
      popEnterTransition = popEnterVerticalTransition,
      popExitTransition = popExitVerticalTransition
    ) {
      EditPostDetails(backOnClick = { navController.popBackStack() })
    }   //endregion
    //endregion
  }
}