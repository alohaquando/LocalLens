package com.oreomrone.locallens.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.oreomrone.locallens.ui.theme.LocalLensTheme

@Composable
fun Alert(
  icon: ImageVector = Icons.Rounded.Info,
  titleText: String = "Title text",
  subtitleText: String = "Subtitle text"
) {
  Column(
    modifier = Modifier
      .padding(horizontal = 16.dp, vertical = 16.dp)
      .fillMaxWidth()
      .background(
        MaterialTheme.colorScheme.secondaryContainer,
        RoundedCornerShape(40.dp)
      )
      .padding(
        vertical = 32.dp,
        horizontal = 48.dp
      ),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(
      12.dp,
      Alignment.CenterVertically
    )
  ) {
    Icon(
      imageVector = icon,
      contentDescription = "Info icon",
      tint = MaterialTheme.colorScheme.onSecondaryContainer,
      modifier = Modifier
        .size(32.dp)
    )
    Text(
      text = titleText,
      color = MaterialTheme.colorScheme.onSecondaryContainer,
      style = MaterialTheme.typography.titleMedium,
      textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(1.dp))
    Text(
      text = subtitleText,
      color = MaterialTheme.colorScheme.onSecondaryContainer,
      textAlign = TextAlign.Center
    )
  }
}

@Preview(
  showBackground = true,
  showSystemUi = true,
  wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
  uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun AlertPreview() {
  LocalLensTheme {
    Alert(
      Icons.Rounded.VerifiedUser,
      "You are using a Super User account",
      "You can edit all posts and promote posts for free"
    )
  }
}