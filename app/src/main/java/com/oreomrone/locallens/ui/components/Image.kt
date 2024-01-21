package com.oreomrone.locallens.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.oreomrone.locallens.ui.utils.conditional

@Composable
fun Image(
  modifier: Modifier = Modifier,
  boxModifier: Modifier = Modifier,
  imageModifier: Modifier = Modifier,
  model: Any? = null,
  contentDescription: String = "Image",
  background: Color = MaterialTheme.colorScheme.secondaryContainer,
  clickable: Boolean = true,
  onClick: () -> Unit = {}
) {
  var isLoading by remember {
    mutableStateOf(false)
  }
  Box(
    modifier = Modifier
      .then(modifier)
      .then(boxModifier)
      .background(background)
      .conditional(clickable) {
        clickable { onClick() }
      },
    contentAlignment = Alignment.Center
  ) {
    if (model != null) {
      val request =
        ImageRequest.Builder(LocalContext.current).data(model).allowHardware(false).build()
      AsyncImage(
        model = request,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        onLoading = { isLoading = true },
        onSuccess = { isLoading = false },
        modifier = Modifier
          .then(modifier)
          .then(imageModifier)
      )
    } else {
      if (isLoading) {
        CircularProgressIndicator(
          modifier = Modifier.width(48.dp),
          color = MaterialTheme.colorScheme.secondary,
          trackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
        )
      } else {
        Icon(
          imageVector = Icons.Rounded.Image,
          contentDescription = "Image placeholder",
          tint = MaterialTheme.colorScheme.onSecondaryContainer,
          modifier = Modifier.size(16.dp)
        )
      }
    }
  }
}

@Preview(
  showBackground = true,
  showSystemUi = true
)
@Composable
private fun ImagePreview() {
  Image()
}