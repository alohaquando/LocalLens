package com.oreomrone.locallens.ui.components.layouts

import android.Manifest
import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.oreomrone.locallens.domain.Place
import com.oreomrone.locallens.domain.PostVisibilities
import com.oreomrone.locallens.ui.components.BottomButtonBar
import com.oreomrone.locallens.ui.components.ErrorAwareOutlinedTextField
import com.oreomrone.locallens.ui.components.Image
import com.oreomrone.locallens.ui.components.ImageSourceSelectSheet
import com.oreomrone.locallens.ui.components.PlaceSearchSheet
import com.oreomrone.locallens.ui.theme.LocalLensTheme
import com.oreomrone.locallens.ui.utils.uriToByteArray
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale

@OptIn(
  ExperimentalMaterial3Api::class,
  ExperimentalComposeUiApi::class,
  ExperimentalPermissionsApi::class
)
@Composable
fun PostInputLayout(
  title: String,
  navigationIcon: ImageVector,
  submitButtonText: String,
  caption: String,
  imageURL: String?,
  inputValid: Boolean,
  captionValid: Boolean,
  alertDialogTitle: String = "Are you sure?",
  alertDialogText: String = "You will lose all your progress if you leave this page.",
  alertDialogConfirmText: String = "Leave",
  imageChangeEnabled: Boolean = true,
  snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
  navigationButtonOnClick: suspend () -> Unit = {},
  onCaptionChange: (String) -> Unit = {},
  onImageChange: (String) -> Unit = {},
  onImageRemove: () -> Unit = {},
  onImageFileChange: (ByteArray) -> Unit = {},
  submitOnClick: suspend () -> Unit = {},
  showPlaceError: Boolean = false,
  placeSearchQuery: String = "",
  onPlaceSearchQueryChange: (String) -> Unit = {},
  placeSearchResult: List<Place> = listOf(),
  placeResultOnClick: (Place) -> Unit = {},
  onPlaceSearchEnter: () -> Unit = {},
  selectedPlace: Place? = null,
  visibility: String = PostVisibilities.PUBLIC.name,
  onVisibilityChange: (String) -> Unit = {},
) {
  // Context
  val context = LocalContext.current
  // Coroutine scope
  val coroutineScope = rememberCoroutineScope()

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

  //region UI-related
  // Scroll behavior
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

  // To hide keyboard on submit
  val keyboardController = LocalSoftwareKeyboardController.current

  // Cancel alert dialog
  var showAlertDialog by remember { mutableStateOf(false) }

  // Image source select sheet
  var showImageSourceSheet by remember { mutableStateOf(false) }

  // Place search sheet
  var showPlaceSearchSheet by remember { mutableStateOf(false) }

  // Visibility menu
  var showVisibilityMenu by remember { mutableStateOf(false) }
  //endregion

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
            if (Uri.parse(imageURL).scheme?.contains("https") == true) {
              onImageFileChange(byteArrayOf())
            } else {
              val image = uriToByteArray(
                contentResolver,
                Uri.parse(imageURL)
              )
              onImageFileChange(image)
            }
            keyboardController?.hide()
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
        .padding(horizontal = 16.dp)
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
          model = imageURL,
          onClick = {
            if (imageChangeEnabled) showImageSourceSheet = true
          })
        if (imageChangeEnabled) {
          Row {
            if (imageURL.isNullOrBlank()) {
              TextButton(onClick = {
                showImageSourceSheet = true
              }) {
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
      }
      //endregion

      OutlinedTextField(value = when {
        selectedPlace != null -> selectedPlace.name
        else                  -> ""
      },
        readOnly = true,
        interactionSource = remember { MutableInteractionSource() }.also { interactionSource ->
          LaunchedEffect(interactionSource) {
            interactionSource.interactions.collect {
              if (it is PressInteraction.Release) {
                showPlaceSearchSheet = true
//                focusRequester.requestFocus()
              }
            }
          }
        },
        isError = showPlaceError,
        supportingText = {
          Text(
            text = when {
              selectedPlace == null && showPlaceError -> "Please pick a place"
              selectedPlace != null                   -> selectedPlace.address
              else                                    -> ""
            }
          )
        },
        onValueChange = { },
        leadingIcon = {
          Icon(
            imageVector = Icons.Rounded.LocationOn,
            contentDescription = "Place",
          )
        },
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
          Text(
            text = "Place"
          )
        },
        label = {
          Text(
            text = "Place"
          )
        })

      ErrorAwareOutlinedTextField(value = caption,
        supportingText = {
          Text(
            text = "Please make sure your caption is not empty"
          )
        },
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
        isError = !captionValid,
        onValueChange = { onCaptionChange(it) },
        modifier = Modifier
          .fillMaxWidth()
          .height(164.dp),
        placeholder = {
          Text(
            text = "Caption"
          )
        },
        label = {
          Text(
            text = "Caption"
          )
        })

      val visibilityOptions = PostVisibilities.entries.map { it.name }

      ExposedDropdownMenuBox(expanded = showVisibilityMenu,
        onExpandedChange = {
          showVisibilityMenu = !showVisibilityMenu
        }) {
        OutlinedTextField(readOnly = true,
          value = visibility.lowercase().replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.ROOT)
            else it.toString()
          },
          onValueChange = { },
          label = { Text("Visible to") },
          placeholder = { Text("Visible to") },
          trailingIcon = {
            ExposedDropdownMenuDefaults.TrailingIcon(
              expanded = showVisibilityMenu
            )
          },
          modifier = Modifier
            .menuAnchor()
            .fillMaxWidth(),
          supportingText = {
            when (visibility) {
              PostVisibilities.PUBLIC.name -> {
                Text(
                  text = "Anyone can see this post"
                )
              }

              PostVisibilities.PRIVATE.name -> {
                Text(
                  text = "Only you and your follower can see this post"
                )
              }

              PostVisibilities.ME.name -> {
                Text(
                  text = "Only you can see this post"
                )
              }

              else -> {
                Text(
                  text = ""
                )
              }
            }
          })
        ExposedDropdownMenu(expanded = showVisibilityMenu,
          onDismissRequest = {
            showVisibilityMenu = false
          }) {
          visibilityOptions.forEach { selectionOption ->
            DropdownMenuItem(onClick = {
              onVisibilityChange(selectionOption)
              showVisibilityMenu = false
            },
              text = {
                Text(text = selectionOption.lowercase().replaceFirstChar {
                  if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                })
              })
          }
        }
      }

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

    if (showPlaceSearchSheet) {
      PlaceSearchSheet(
        onDismissRequest = { showPlaceSearchSheet = false },
        placeSearchQuery = placeSearchQuery,
        onPlaceSearchQueryChange = onPlaceSearchQueryChange,
        onPlaceSearchEnter = onPlaceSearchEnter,
        placeSearchResult = placeSearchResult,
        placeResultOnClick = placeResultOnClick,
      )
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
fun PostLayoutPreview() {
  LocalLensTheme {
    PostInputLayout(
      title = "New post",
      navigationIcon = Icons.Rounded.Close,
      submitButtonText = "Post",
      caption = "",
      imageURL = "",
      inputValid = false,
      showPlaceError = false,
      captionValid = false,
    )
  }
}