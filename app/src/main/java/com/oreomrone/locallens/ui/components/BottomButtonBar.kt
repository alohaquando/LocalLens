package com.oreomrone.locallens.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp

@Composable
fun BottomButtonBar(
  text: String = "Text",
  onClick: () -> Unit = {},
  enabled: Boolean = true,
) {
  Surface(
    modifier = Modifier.fillMaxWidth(),
    color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
  ) {
    Box(
      modifier = Modifier
        .padding(
          horizontal = 16.dp,
          vertical = 8.dp
        )
        .padding(
          bottom = WindowInsets.systemBars
            .asPaddingValues()
            .calculateBottomPadding()
        )
    ) {
      Button(
        enabled = enabled,
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(text)
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
private fun BottomButtonBarPreview(

) {
  BottomButtonBar()
}