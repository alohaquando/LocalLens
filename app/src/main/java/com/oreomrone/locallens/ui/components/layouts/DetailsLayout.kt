package com.oreomrone.locallens.ui.components.layouts

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.oreomrone.locallens.ui.components.Image
import com.oreomrone.locallens.ui.components.LoadingOverlay
import com.oreomrone.locallens.ui.components.Post
import com.oreomrone.locallens.ui.components.StatItemButton
import com.oreomrone.locallens.ui.theme.LocalLensTheme

@OptIn(
  ExperimentalMaterial3Api::class,
  ExperimentalLayoutApi::class
)
@Composable
fun DetailsLayout(
  title: String = "",
  subtitle: String = "",
  ternaryText: String = "",
  image: String = "",
  showBackButton: Boolean = false,
  backOnClick: () -> Unit = {},
  isLoading: Boolean = false,
  isPrivate: Boolean = false,
  buttonsRowContent: @Composable () -> Unit = {},
  statsRowContent: @Composable () -> Unit = {},
  content: @Composable () -> Unit = {},
) {
  val configuration = LocalConfiguration.current
  val screenWidth = with(LocalDensity.current) { configuration.screenWidthDp.dp.toPx() }
  val offsetShowTopBar = -(screenWidth)

  Crossfade(
    targetState = isLoading,
    label = "Details layout animation for $title"
  ) {
    when (it) {

      // Loading
      true -> {
        LoadingOverlay()
      }

      // Content
      false -> {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

        Scaffold(Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
          topBar = {
            CenterAlignedTopAppBar(title = {
              Text(
                modifier = Modifier.padding(horizontal = 24.dp),
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            },
              scrollBehavior = scrollBehavior,
              colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = if (scrollBehavior.state.contentOffset >= offsetShowTopBar) {
                  Color.Transparent
                } else {
                  MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                },
                titleContentColor = if (scrollBehavior.state.contentOffset >= offsetShowTopBar) {
                  Color.Transparent
                } else {
                  MaterialTheme.colorScheme.onSurface
                },

                ),

              navigationIcon = {
                if (showBackButton) {
                  FilledTonalIconButton(
                    onClick = backOnClick,
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                      containerColor = if (scrollBehavior.state.contentOffset >= offsetShowTopBar) {
                        MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                      } else {
                        Color.Transparent
                      }
                    )
                  ) {
                    Icon(
                      imageVector = Icons.Rounded.ArrowBack,
                      contentDescription = "",
                    )
                  }
                }
              }

            )
          }) { innerPadding ->
          Column(
            Modifier.verticalScroll(rememberScrollState())
          ) {
            Box {
              // Profile Image
              Image(
                modifier = Modifier
                  .fillMaxWidth()
                  .aspectRatio(1f),
                model = image,
                contentDescription = "Image for $title",
              )

              // Shadow
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .aspectRatio(1f)
                  .background(
                    brush = Brush.verticalGradient(
                      colors = listOf(
                        Color.Transparent,
                        Color.Transparent,
                        MaterialTheme.colorScheme.surface
                      )
                    )
                  )
              )

              // Text
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .aspectRatio(1f)
                  .padding(bottom = 32.dp)
                  .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                  8.dp,
                  Alignment.Bottom
                )
              ) {
                Text(
                  text = title,
                  style = MaterialTheme.typography.displaySmall,
                  textAlign = TextAlign.Center,
                  maxLines = 3,
                  overflow = TextOverflow.Ellipsis
                )
                Text(
                  text = subtitle,
                  style = MaterialTheme.typography.titleMedium,
                  textAlign = TextAlign.Center,
                  maxLines = 3,
                  overflow = TextOverflow.Ellipsis
                )
              }
            }

            Column(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
              verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
              if (ternaryText.isNotBlank()) {
                Text(
                  text = ternaryText,
                  textAlign = TextAlign.Center,
                  maxLines = 3,
                  overflow = TextOverflow.Ellipsis,
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                )
              }

              if (isPrivate) {
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(
                    8.dp,
                    Alignment.CenterHorizontally
                  )
                ) {
                  Icon(
                    imageVector = Icons.Rounded.Lock,
                    contentDescription = "Lock",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                  )
                  Text(
                    text = "Private",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }

              // Button Row
              buttonsRowContent()

              // Stats Row
              statsRowContent()
            }

            Divider()

            Column {
              content()
            }

            Spacer(modifier = Modifier.height(innerPadding.calculateBottomPadding()))
          }
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
private fun DetailsLayoutPreview() {
  LocalLensTheme {
    DetailsLayout(title = "This is an extremely long title that should be truncated",
      subtitle = "This is an extremely long subtitle that should be truncated",
      ternaryText = "Hello",
      image = "https://picsum.photos/seed/picsum/200/300",
      buttonsRowContent = {
        Button(onClick = { /*TODO*/ }) {
          Text(text = "Button 1")
        }
      },
      statsRowContent = {
        StatItemButton()
        StatItemButton()
        StatItemButton()
        StatItemButton()
      },
      content = {
        Post()
      })
  }
}