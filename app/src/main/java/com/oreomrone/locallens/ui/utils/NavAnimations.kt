package com.oreomrone.locallens.ui.utils

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavBackStackEntry

private const val TIME_DURATION = 350

// Cut

val enterCutTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    EnterTransition.None
}

val exitCutTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    ExitTransition.None
}

val popEnterCutTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
    {
        EnterTransition.None

    }

val popExitCutTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    ExitTransition.None
}

// Fade

val enterFadeTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    fadeIn(
        animationSpec = tween(durationMillis = TIME_DURATION)
    )
}

val exitFadeTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    fadeOut(animationSpec = tween(durationMillis = TIME_DURATION))
}

val popEnterFadeTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
    {
        fadeIn(
            animationSpec = tween(durationMillis = TIME_DURATION)
        )
    }

val popExitFadeTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
    {
        fadeOut(animationSpec = tween(durationMillis = TIME_DURATION))
    }


// Horizontal

val enterHorizontalTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
    {
        slideInHorizontally(
            initialOffsetX = { 200 },
            animationSpec = tween(durationMillis = TIME_DURATION, easing = LinearOutSlowInEasing)
        ) + fadeIn(
            animationSpec = tween(durationMillis = TIME_DURATION)
        )
    }

val exitHorizontalTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
    {
        slideOutHorizontally(
            targetOffsetX = { -it / 3 },
            animationSpec = tween(durationMillis = TIME_DURATION, easing = LinearOutSlowInEasing)
        ) + fadeOut()
    }

val popEnterHorizontalTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
    {
        slideInHorizontally(
            initialOffsetX = { -it / 3 },
            animationSpec = tween(durationMillis = TIME_DURATION, easing = LinearOutSlowInEasing)
        ) + fadeIn(
            animationSpec = tween(durationMillis = TIME_DURATION)
        )
    }

val popExitHorizontalTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
    {
        slideOutHorizontally(
            targetOffsetX = { 200 },
            animationSpec = tween(durationMillis = TIME_DURATION, easing = LinearOutSlowInEasing)
        ) + fadeOut()
    }

// Vertical

val enterVerticalTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
    {
        slideInVertically(
            initialOffsetY = { 200 },
            animationSpec = tween(durationMillis = TIME_DURATION, easing = LinearOutSlowInEasing)
        ) + fadeIn(initialAlpha = 0.3f)
    }

val exitVerticalTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
    {
        slideOutVertically(
            targetOffsetY = { -40 },
            animationSpec = tween(durationMillis = TIME_DURATION, easing = LinearOutSlowInEasing)
        ) + fadeOut(animationSpec = tween(durationMillis = TIME_DURATION))
    }

val popEnterVerticalTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
    {
        slideInVertically(
            initialOffsetY = { -40 },
            animationSpec = tween(durationMillis = TIME_DURATION, easing = LinearOutSlowInEasing)
        ) + fadeIn(initialAlpha = 0.3f)
    }

val popExitVerticalTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
    {
        slideOutVertically(
            targetOffsetY = { 200 },
            animationSpec = tween(durationMillis = TIME_DURATION, easing = LinearOutSlowInEasing)
        ) + fadeOut(animationSpec = tween(durationMillis = TIME_DURATION))
    }