package com.oreomrone.locallens.ui.components.layouts

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.oreomrone.locallens.ui.theme.LocalLensTheme

@Composable
fun ErrorOverlay(
  errorMessage: String = "An error occurred",
  backOnClick: () -> Unit = {},
  showBackButton: Boolean = true,
) {
  Surface(
    modifier = Modifier.fillMaxSize(),
    color = MaterialTheme.colorScheme.surface
  ) {
    Column(
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(
        8.dp,
        Alignment.CenterVertically
      )
    ) {
      Icon(
        imageVector = Icons.Rounded.Error,
        contentDescription = "Error",
      )
      Text(text = errorMessage)
      if (showBackButton) FilledTonalButton(
        onClick = backOnClick,
        modifier = Modifier.padding(16.dp)
      ) {
        Text(text = "Go back")
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
fun ErrorOverlayPreview(

) {
  LocalLensTheme {
    ErrorOverlay()
  }
}