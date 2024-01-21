package com.oreomrone.locallens.ui.tests

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.oreomrone.locallens.ui.components.Image
import java.io.File


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraTest(
  viewModel: CameraTestViewModel = hiltViewModel()
) {
  val cameraPermissionState = rememberPermissionState(
    android.Manifest.permission.CAMERA
  )

  var pictureTaken by remember { mutableStateOf(false) }

  val context = LocalContext.current

  val file = File(
    context.cacheDir,
    "post_image_${System.currentTimeMillis()}.png"
  )

  val uri = FileProvider.getUriForFile(
    context,
    context.packageName + ".provider",
    file
  )

  val cameraLauncher =
    rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSuccessful ->
      pictureTaken = isSuccessful
      viewModel.onImageFileChange(file.readBytes())
      viewModel.onImageChange(
        uri.toString()
      )
    }

  LaunchedEffect(Unit) {
    if (cameraPermissionState.status.isGranted) {
      cameraLauncher.launch(uri)
    }
  }

  val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

  Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
  ) {
    Column(
      Modifier
        .padding(horizontal = 16.dp)
        .verticalScroll(rememberScrollState()),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {

      when {
        cameraPermissionState.status.isGranted -> {
          Text("Camera permission Granted")
          Button(onClick = { cameraLauncher.launch(uri) }) {
            Text(text = "Take a picture")
          }

          if (pictureTaken) {
            Image(
              modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(24.dp)),
              model = uiState.imageURL,
            )
          }
        }

        else                                   -> {
          Text(
            when {
              cameraPermissionState.status.shouldShowRationale -> {
                "The camera is important for this app. Please grant the permission."
              }

              else                                             -> {
                "Camera permission required for this feature to be available. " + "Please grant the permission"
              }
            }
          )

          Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
            Text("Request permission")
          }
        }
      }
    }
  }
}