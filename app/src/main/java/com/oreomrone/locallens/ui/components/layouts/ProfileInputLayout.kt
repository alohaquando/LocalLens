package com.oreomrone.locallens.ui.components.layouts

import android.Manifest
import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AlternateEmail
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.oreomrone.locallens.ui.components.BottomButtonBar
import com.oreomrone.locallens.ui.components.ErrorAwareOutlinedTextField
import com.oreomrone.locallens.ui.components.Image
import com.oreomrone.locallens.ui.components.ImageSourceSelectSheet
import com.oreomrone.locallens.ui.theme.LocalLensTheme
import com.oreomrone.locallens.ui.utils.uriToByteArray
import kotlinx.coroutines.launch
import java.io.File

@OptIn(
  ExperimentalMaterial3Api::class,
  ExperimentalComposeUiApi::class,
  ExperimentalPermissionsApi::class
)
@Composable
fun ProfileInputLayout(
  title: String,
  navigationIcon: ImageVector,
  submitButtonText: String,
  username: String,
  name: String,
  bio: String,
  isPrivate: Boolean = false,
  onIsPrivateChange: (Boolean) -> Unit = {},
  imageURL: String?,
  inputValid: Boolean,
  usernameValid: Boolean,
  nameValid: Boolean,
  bioValid: Boolean,
  alertDialogTitle: String = "Are you sure?",
  alertDialogText: String = "You will lose all your progress if you leave this page.",
  alertDialogConfirmText: String = "Leave",
  snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
  navigationButtonOnClick: suspend () -> Unit = {},
  onNameChange: (String) -> Unit = {},
  onUsernameChange: (String) -> Unit = {},
  onBioChange: (String) -> Unit = {},
  onImageChange: (String) -> Unit = {},
  onImageRemove: () -> Unit = {},
  onImageFileChange: (ByteArray) -> Unit = {},
  submitOnClick: suspend () -> Unit = {},
) {
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
  val coroutineScope = rememberCoroutineScope()

  val keyboardController = LocalSoftwareKeyboardController.current

  var showAlertDialog by remember { mutableStateOf(false) }

  // Context
  val context = LocalContext.current

  //region Gallery functions
  val galleryLauncher =
    rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
      uri?.let {
        if (it.toString() != imageURL) {
          onImageChange(it.toString())
        }
      }
    }

  val contentResolver = LocalContext.current.contentResolver
  //endregion

  //region Camera functions
  val cameraPermissionState = rememberPermissionState(
    Manifest.permission.CAMERA
  )

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
    rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { _ ->
      onImageFileChange(file.readBytes())
      onImageChange(
        uri.toString()
      )
    }

  // Image source select sheet
  var showImageSourceSheet by remember { mutableStateOf(false) }

  Scaffold(modifier = Modifier
    .nestedScroll(scrollBehavior.nestedScrollConnection)
    .imePadding(),
    topBar = {
      CenterAlignedTopAppBar(
        title = {
          Text(title)
        },
        navigationIcon = {
          IconButton(onClick = {
            keyboardController?.hide()
            showAlertDialog = true
          }) {
            Icon(
              imageVector = navigationIcon,
              contentDescription = "Close"
            )
          }
        },
        scrollBehavior = scrollBehavior
      )
    },
    bottomBar = {
      BottomButtonBar(text = submitButtonText,
        enabled = inputValid,
        onClick = {
          coroutineScope.launch {
            keyboardController?.hide()

            if (Uri.parse(imageURL).scheme?.contains("https") == true) {
              onImageFileChange(byteArrayOf())
            } else {
              val image = uriToByteArray(
                contentResolver,
                Uri.parse(imageURL)
              )
              onImageFileChange(image)
            }

            submitOnClick()
          }
        })
    },
    snackbarHost = { SnackbarHost(snackbarHostState) }) {

    // Content
    Column(
      Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(
          top = it.calculateTopPadding()
        )
        .imePadding(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(
        space = 8.dp
      ),
    ) {
      Spacer(modifier = Modifier.height(8.dp))

      //region Image
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Image(modifier = Modifier
          .fillMaxWidth()
          .animateContentSize()
          .aspectRatio(if (imageURL.isNullOrBlank()) 2f else 1f)
          .clip(
            RoundedCornerShape(32.dp)
          ),
          paddingModifier = Modifier.padding(horizontal = 16.dp),
          model = imageURL,
          onClick = {
            showImageSourceSheet = true
          })
        Row {
          if (imageURL.isNullOrBlank()) {
            TextButton(onClick = { showImageSourceSheet = true }) {
              Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Add"
              )
              Text(text = "Add image")
            }
          } else {
            TextButton(onClick = { showImageSourceSheet = true }) {
              Icon(
                imageVector = Icons.Outlined.Image,
                contentDescription = "Change",
                modifier = Modifier
                  .padding(end = 4.dp)
                  .size(18.dp)
              )
              Text(text = "Change")
            }
            TextButton(onClick = { onImageRemove() }) {
              Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Remove",
                modifier = Modifier
                  .padding(end = 4.dp)
                  .size(18.dp)
              )
              Text(text = "Remove")
            }
          }
        }
      }
      //endregion

      ErrorAwareOutlinedTextField(value = username,
        supportingText = {
          Text(
            text = "Please pick an unique username and make sure it's at least 3 characters long." + " Please don't use special characters or spaces."
          )
        },
        isError = !usernameValid,
        onValueChange = { onUsernameChange(it) },
        leadingIcon = {
          Icon(
            imageVector = Icons.Rounded.AlternateEmail,
            contentDescription = "Username",
          )
        },
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        placeholder = {
          Text(
            text = "Username"
          )
        },
        label = {
          Text(
            text = "Username"
          )
        })

      ErrorAwareOutlinedTextField(value = name,
        supportingText = {
          Text(
            text = "Please make sure your name is not empty"
          )
        },
        isError = !nameValid,
        onValueChange = { onNameChange(it) },
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        placeholder = {
          Text(
            text = "Name"
          )
        },
        label = {
          Text(
            text = "Name"
          )
        })

      ErrorAwareOutlinedTextField(value = bio,
        supportingText = {
          Text(
            text = "Please make sure your bio is not empty"
          )
        },
        isError = !bioValid,
        onValueChange = { onBioChange(it) },
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        placeholder = {
          Text(
            text = "Bio"
          )
        },
        label = {
          Text(
            text = "Bio"
          )
        })

      ListItem(
        headlineContent = {
          Text(
            text = "Private account"
          )
        },
        supportingContent = {
          Text(
            text = "Other people will have to request before they can follow you"
          )
        },
        trailingContent = {
          Switch(
            checked = isPrivate,
            onCheckedChange = onIsPrivateChange
          )
        },
      )

      Spacer(modifier = Modifier.height(it.calculateBottomPadding()))
    }



    if (showAlertDialog) {
      AlertDialog(title = {
        Text(text = alertDialogTitle)
      },
        text = {
          Text(
            text = alertDialogText
          )
        },
        onDismissRequest = {
          showAlertDialog = false
        },
        confirmButton = {
          TextButton(onClick = {
            coroutineScope.launch {
              navigationButtonOnClick()
            }
          }) {
            Text(alertDialogConfirmText)
          }
        },
        dismissButton = {
          TextButton(onClick = {
            showAlertDialog = false
          }) {
            Text("Stay and continue")
          }
        })
    }

    if (showImageSourceSheet) {
      ImageSourceSelectSheet(onDismissRequest = { showImageSourceSheet = false },
        cameraOnClick = {
          if (cameraPermissionState.status.isGranted) {
            cameraLauncher.launch(uri)
          } else {
            cameraPermissionState.launchPermissionRequest()
          }
        },
        cameraPermissionText = when {
          cameraPermissionState.status.isGranted -> ""
          else                                   -> "Camera permission required"
        },
        galleryOnClick = {
          galleryLauncher.launch("image/*")
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
fun ProfileLayoutPreview() {
  LocalLensTheme {
    ProfileInputLayout(
      title = "Complete your profile",
      navigationIcon = Icons.Rounded.Close,
      submitButtonText = "Complete",
      username = "",
      name = "",
      bio = "",
      imageURL = "",
      inputValid = false,
      usernameValid = false,
      nameValid = false,
      bioValid = false,
    )
  }
}