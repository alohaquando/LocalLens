package com.oreomrone.locallens.ui.screens.auth.resetPassword

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.oreomrone.locallens.ui.components.BottomButtonBar
import com.oreomrone.locallens.ui.theme.LocalLensTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthResetPasswordCheckEmail(
  navigationButtonOnClick: () -> Unit = {},
) {
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

  Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection).imePadding(),
    topBar = {
      CenterAlignedTopAppBar(title = {
        Text("Check your email")
      },
        navigationIcon = {
          IconButton(onClick = navigationButtonOnClick) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Back",
            )
          }
        })
    },
    bottomBar = {
      BottomButtonBar(
        text = "Back to sign in",
        onClick = navigationButtonOnClick,
      )
    }) { innerPadding ->
    Column(
      Modifier
        .padding(top = innerPadding.calculateTopPadding())
        .padding(horizontal = 16.dp)
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
        .imePadding(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(
        space = 32.dp,
      ),
    ) {
      Column(
        verticalArrangement = Arrangement.spacedBy(
          space = 8.dp,
          alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Spacer(modifier = Modifier.padding(top = 8.dp))

        Text(text = "We've sent an email to you. Please check your inbox and follow the instructions to reset your password.")
      }
    }
  }
}

@Preview(
  showBackground = true,
  showSystemUi = true,
  wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
  uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AuthResetPasswordCheckEmailPreview(

) {
  LocalLensTheme {
    AuthResetPasswordCheckEmail()
  }
}