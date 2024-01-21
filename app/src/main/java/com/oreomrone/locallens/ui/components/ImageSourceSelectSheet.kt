package com.oreomrone.locallens.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.oreomrone.locallens.ui.theme.LocalLensTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageSourceSelectSheet(
  onDismissRequest: () -> Unit = { },
  cameraOnClick: () -> Unit = { },
  cameraPermissionText: String = "",
  galleryOnClick: () -> Unit = { },
) {
  ModalBottomSheet(
    onDismissRequest = onDismissRequest,
    windowInsets = WindowInsets.ime,
    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  ) {
    Column(
      modifier = Modifier.padding(
        bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
      )
    ) {
      Text(
        text = "Get image from",
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier.padding(
          horizontal = 16.dp,
          vertical = 8.dp
        )
      )
      ListItem(headlineContent = { Text(text = "Camera") },
        leadingContent = {
          Icon(
            imageVector = Icons.Rounded.CameraAlt,
            contentDescription = "Camera",
          )
        },
        trailingContent = { Text(text = cameraPermissionText) },
        modifier = Modifier.
        clickable {
          onDismissRequest()
          cameraOnClick()
        })
      ListItem(headlineContent = { Text(text = "Gallery") },
        leadingContent = {
          Icon(
            imageVector = Icons.Rounded.Image,
            contentDescription = "Gallery",
          )
        },
        modifier = Modifier.clickable {
          onDismissRequest()
          galleryOnClick()
        })
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
private fun ImageSourceSelectSheetPreview(

) {
  LocalLensTheme {
    ImageSourceSelectSheet()
  }
}