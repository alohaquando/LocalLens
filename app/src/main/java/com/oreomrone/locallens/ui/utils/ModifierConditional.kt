package com.oreomrone.locallens.ui.utils

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@SuppressLint("ComposableModifierFactory")
@Composable
fun Modifier.conditional(condition : Boolean, modifier : @Composable Modifier.() -> Modifier) : Modifier {
    return if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}