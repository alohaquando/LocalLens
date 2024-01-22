package com.oreomrone.locallens.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.oreomrone.locallens.R
import com.oreomrone.locallens.ui.theme.LocalLensTheme

@Composable
fun PostMapMarker(
  imageModel: Any? = null,
) {
  Box {
    Icon(
      painter = painterResource(id = R.drawable.post_map_marker),
      "Post Map Marker",
      tint = MaterialTheme.colorScheme.primaryContainer,
    )
    Box(
      modifier = Modifier.padding(6.dp),
      contentAlignment = Alignment.Center
    ) {
      Image(
        modifier = Modifier
          .size(76.dp)
          .clip(RoundedCornerShape(4.dp)),
        background = MaterialTheme.colorScheme.primary,
        model = imageModel,
        contentDescription = "Post Map Marker",
        clickable = true,
      )
    }
  }
}

@Preview(
  showBackground = true,
  showSystemUi = true,
  wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
  uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun PostMapMarkerPreview() {
  LocalLensTheme {
    PostMapMarker()
  }
}