package com.oreomrone.locallens.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
fun PostMapCluster(text: String = "5") {
  Surface(
    modifier = Modifier.size(80.dp),
    color = MaterialTheme.colorScheme.primary,
    shape = CircleShape,
    border = BorderStroke(
      8.dp,
      MaterialTheme.colorScheme.primaryContainer
    ),
  ) {
    Box(
      modifier = Modifier,
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = text,
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.titleLarge
      )
    }
  }
}

@Preview(
  showBackground = true,
  showSystemUi = true,
  wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE
)
@Composable
private fun PostMapClusterPreview() {
  LocalLensTheme {
    PostMapCluster()
  }
}