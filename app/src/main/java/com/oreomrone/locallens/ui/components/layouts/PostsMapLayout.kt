package com.oreomrone.locallens.ui.components.layouts

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.rememberCameraPositionState
import com.oreomrone.locallens.R
import com.oreomrone.locallens.domain.Post
import com.oreomrone.locallens.ui.components.Post
import com.oreomrone.locallens.ui.components.PostMapCluster
import com.oreomrone.locallens.ui.components.PostMapMarker
import com.oreomrone.locallens.ui.theme.LocalLensTheme
import com.oreomrone.locallens.ui.utils.PostClusterItem
import kotlinx.coroutines.launch

@OptIn(
  ExperimentalMaterial3Api::class,
  MapsComposeExperimentalApi::class
)
@Composable
fun PostsMapLayout(
  posts: List<Post>? = null,
  postsClusterItems: List<PostClusterItem>,
  mapStartingPosition: LatLng,
  notificationsOnClick: () -> Unit = {},
  placeOnClick: (String) -> Unit = {},
  userOnClick: (String) -> Unit = {},
  editOnClick: (String) -> Unit = {},
  navigateOnClick: (Double, Double, String) -> Unit = { _, _, _ -> },
  favoriteOnClick: suspend (String) -> Unit = {},
  deleteOnClick: suspend (String) -> Unit = {},
  showBackButton: Boolean = false,
  backOnClick: () -> Unit = {},
) {
  val coroutineScope = rememberCoroutineScope()
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

  // For initial height of sheet
  val configuration = LocalConfiguration.current
  val screenHeight = configuration.screenHeightDp.dp

  // Details for single post
  var clickedPost by remember { mutableStateOf<Post?>(null) }
  var showModalBottomSheet by remember {
    mutableStateOf(false)
  }

  // Scaffold state
  val scaffoldState = rememberBottomSheetScaffoldState(
    bottomSheetState = rememberStandardBottomSheetState(
      initialValue = if (posts != null) {
        SheetValue.PartiallyExpanded
      } else {
        SheetValue.Hidden
      },
      skipHiddenState = posts != null,
    )
  )

  BottomSheetScaffold(scaffoldState = scaffoldState,
    sheetDragHandle = {},
    sheetPeekHeight = screenHeight / 3,
    // Sheet content
    sheetContent = {

      // Sheet content - Scaffold
      Scaffold(modifier = Modifier
        .nestedScroll(scrollBehavior.nestedScrollConnection)
        .imePadding(),
        topBar = {
          CenterAlignedTopAppBar(
            title = { Text(text = "Posts") },
            actions = {
              IconButton(onClick = notificationsOnClick) {
                Icon(
                  imageVector = Icons.Outlined.Notifications,
                  contentDescription = "Notifications",
                )
              }
            },
            scrollBehavior = scrollBehavior
          )
        }) {
        if (!posts.isNullOrEmpty()) {
          // Sheet content - Posts
          Column(
            modifier = Modifier
              .verticalScroll(rememberScrollState())
              .padding(it)
          ) {
            for (post in posts) {
              Post(
                place = post.place.name,
                address = post.place.address,
                caption = post.caption,
                username = post.user?.username.toString(),
                date = post.timestamp,
                favorites = post.favorites.size,
                postImageModel = post.image,
                userImageModel = post.user?.image.toString(),
                isFavorite = false, // TODO
                showDivider = true,
                showUser = true,
                showMenuButton = false, // TODO
                navigateOnClick = {
                  navigateOnClick(
                    post.place.latitude,
                    post.place.longitude,
                    post.place.name
                  )
                },
                favoriteOnClick = {
                  coroutineScope.launch {
                    favoriteOnClick(post.id)
                  }
                },
                placeOnClick = { placeOnClick(post.place.id) },
                userOnClick = { userOnClick(post.user?.id.toString()) },
                editOnClick = { editOnClick(post.id) },
                deleteOnClick = {
                  coroutineScope.launch {
                    deleteOnClick(post.id)
                  }
                },
              )
            }

            Spacer(modifier = Modifier.height(it.calculateBottomPadding()))
          }
        }
      }
    }) {
    // Content behind sheet
    Box(modifier = Modifier.fillMaxSize()) {
      GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = rememberCameraPositionState {
          position = CameraPosition.fromLatLngZoom(
            mapStartingPosition,
            15f
          )
        },
        properties = MapProperties(
          mapStyleOptions = if (isSystemInDarkTheme()) {
            MapStyleOptions.loadRawResourceStyle(
              LocalContext.current,
              R.raw.map_styles
            )
          } else {
            null
          }
        ),
        uiSettings = MapUiSettings(
          zoomControlsEnabled = false,
          compassEnabled = false,
          mapToolbarEnabled = true
        ),
      ) {
        Clustering(items = postsClusterItems,
          clusterContent = { cluster ->
            PostMapCluster(cluster.size.toString())
          },
          clusterItemContent = { PostMapMarker(it.post?.image) },
          onClusterItemClick = {
            clickedPost = it.post
            showModalBottomSheet = true
            true
          })
      }

      Box(
        modifier = Modifier
          .padding(
            top = WindowInsets.systemBars
              .asPaddingValues()
              .calculateTopPadding(),
          )
          .padding(
            vertical = 8.dp,
            horizontal = 12.dp
          )
      ) {
        if (showBackButton) {
          FilledTonalIconButton(onClick = backOnClick) {
            Icon(
              imageVector = Icons.Outlined.ArrowBack,
              contentDescription = "Back",
            )
          }
        }
      }
    }

    if (showModalBottomSheet && clickedPost != null) {
      ModalBottomSheet(sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
      ),
        dragHandle = null,
        windowInsets = WindowInsets.ime,
        onDismissRequest = {
          showModalBottomSheet = false
        }) {
        Column {
          Post(place = clickedPost!!.place.name,
            address = clickedPost!!.place.address,
            caption = clickedPost!!.caption,
            username = clickedPost!!.user?.username.toString(),
            date = clickedPost!!.timestamp,
            favorites = clickedPost!!.favorites.size,
            postImageModel = clickedPost!!.image,
            userImageModel = clickedPost!!.user?.image.toString(),
            isFavorite = false, // TODO
            showDivider = false,
            showUser = true,
            showMenuButton = false, // TODO
            navigateOnClick = {
              navigateOnClick(
                clickedPost!!.place.latitude,
                clickedPost!!.place.longitude,
                clickedPost!!.place.name
              )
            },
            favoriteOnClick = {
              coroutineScope.launch {
                favoriteOnClick(clickedPost!!.id)
              }
            },
            placeOnClick = {
              showModalBottomSheet = false
              placeOnClick(clickedPost!!.place.id) },
            userOnClick = {
              showModalBottomSheet = false
              userOnClick(clickedPost!!.user?.id.toString()) },
            editOnClick = {
              showModalBottomSheet = false
              editOnClick(clickedPost!!.id) },
            deleteOnClick = {
              showModalBottomSheet = false
              coroutineScope.launch {
                deleteOnClick(clickedPost!!.id)
              }
            })
          Spacer(
            modifier = Modifier.height(
              WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
            )
          )
        }
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
fun PostsMapPreview() {
  LocalLensTheme {
    PostsMapLayout(
      postsClusterItems = listOf(),
      mapStartingPosition = LatLng(
        10.777263208853345,
        106.69534102887356
      ),
      showBackButton = true
    )
  }
}