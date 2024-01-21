package com.oreomrone.locallens.ui.components

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.oreomrone.locallens.ui.theme.LocalLensTheme

@Composable
fun BottomTextMessageBar(
  value: String = "",
  onValueChange: (String) -> Unit = {},
  buttonOnClick: () -> Unit = {},
) {
  Surface(
    modifier = Modifier.fillMaxWidth()
  ) {
    Box(
      modifier = Modifier
        .imePadding()
        .padding(
          horizontal = 16.dp,
          vertical = 4.dp
        )
        .padding(
          bottom = WindowInsets.systemBars
            .asPaddingValues()
            .calculateBottomPadding()
        )
    ) {
      TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = "Text message") },
        trailingIcon = {
          FilledIconButton(
            onClick = buttonOnClick,
            enabled = value.isNotBlank(),
            modifier = Modifier.padding(end = 4.dp)
          ) {
            Icon(
              imageVector = Icons.Rounded.Send,
              contentDescription = "",
            )
          }
        },
        maxLines = 4,
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = TextFieldDefaults.colors(
          focusedIndicatorColor = Color.Transparent,
          unfocusedIndicatorColor = Color.Transparent,
          disabledIndicatorColor = Color.Transparent
        )
      )
    }
  }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(
  showBackground = true,
  showSystemUi = true,
  wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
  uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun BottomTextMessageBarPreview() {
  var value by remember {
    mutableStateOf("Text message\n" + "Very long\n" + "Newline\n" + "Text\n")
  }
  LocalLensTheme {
    Scaffold(bottomBar = {
      BottomTextMessageBar(value = value,
        onValueChange = { value = it })
    }) {}
  }
}