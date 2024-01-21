package com.oreomrone.locallens.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oreomrone.locallens.R
import com.oreomrone.locallens.ui.theme.LocalLensTheme

@Composable
fun GoogleAuthButton(onClick: () -> Unit = {}) {
  OutlinedButton(
    onClick = onClick,
    modifier = Modifier.fillMaxWidth(),
    colors = ButtonDefaults.buttonColors(
      containerColor = Color.White,
      contentColor = Color.Black
    )
  ) {
    Icon(
      painter = painterResource(id = R.drawable.google_logo),
      "Google Logo",
      tint = Color.Unspecified,
      modifier = Modifier.size(20.dp)
    )
    Text(
      text = "Continue with Google",
      modifier = Modifier.fillMaxWidth(),
      textAlign = TextAlign.Center,
      color = Color.Black
    )
  }
}

@Preview(
  showBackground = true,
  showSystemUi = true
)
@Composable
private fun AuthenticateWithGoogleButtonPreview() {
  LocalLensTheme {
    GoogleAuthButton()
  }
}