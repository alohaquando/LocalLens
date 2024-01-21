package com.oreomrone.locallens.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.oreomrone.locallens.ui.theme.LocalLensTheme

@Composable
fun StatItemButton(
  modifier: Modifier = Modifier,
  onClick: () -> Unit = {},
  value: String = "Value",
  label: String = "Label"
) {
  TextButton(
    modifier = modifier,
    onClick = { onClick() },
  ) {
    Column(
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = value,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = label,
        maxLines = 1,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center
      )
    }

  }
}

//@Composable
//fun StatItemsRow(list: List<Map<String, String>>) {
//  Row(
//    modifier = Modifier
//      .fillMaxWidth()
//      .padding(horizontal = 16.dp)
//      .height(IntrinsicSize.Min)
//  ) {
//    list.forEach { item ->
//      StatItem(
//        value = item["value"]!!,
//        label = item["label"]!!,
//        modifier = Modifier.weight(1f)
//      )
//      if (list.last() != item) {
//        Divider(
//          modifier = Modifier
//            .fillMaxHeight()
//            .padding(vertical = 16.dp)
//            .width(1.dp)
//        )
//      }
//    }
//  }
//}

@Preview(
  showBackground = true,
  showSystemUi = true,
  wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
  uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun StatItemPreview() {
  LocalLensTheme {
    StatItemButton()
  }
}


