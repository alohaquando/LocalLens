package com.oreomrone.locallens.ui.screens.discover

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oreomrone.locallens.ui.components.Image
import com.oreomrone.locallens.ui.theme.LocalLensTheme
import com.oreomrone.locallens.ui.utils.SampleData

@Composable
fun DiscoverSearch(
  backOnClick: () -> Unit = {},
  placeResultOnClick: (String) -> Unit = {},
  userResultOnClick: (String) -> Unit = {},
) {
  val viewModel: DiscoverSearchViewModel = hiltViewModel()
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  DiscoverSearch(
    uiState = uiState,
    onSearchQueryChange = viewModel::onSearchQueryChange,
    onFilterChange = viewModel::onFilterChange,
    backOnClick = backOnClick,
    placeResultOnClick = placeResultOnClick,
    userResultOnClick = userResultOnClick,
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DiscoverSearch(
  uiState: DiscoverSearchUiState = DiscoverSearchUiState(),
  onSearchQueryChange: (String) -> Unit = {},
  onFilterChange: (DiscoverSearchFilter) -> Unit = {},
  backOnClick: () -> Unit = {},
  placeResultOnClick: (String) -> Unit = {},
  userResultOnClick: (String) -> Unit = {},
) {

  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

  val focusManager = LocalFocusManager.current
  val focusRequester = FocusRequester()

  LaunchedEffect(Unit) {
    focusRequester.requestFocus()
  }

  Scaffold(
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection).imePadding(),
    topBar = {
      Surface(color = MaterialTheme.colorScheme.surface) {
        Column {
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .padding(
                top = WindowInsets.systemBars
                  .asPaddingValues()
                  .calculateTopPadding()
              )
              .padding(
                horizontal = 16.dp,
                vertical = 8.dp
              )
              .height(56.dp)
          ) {
            OutlinedTextField(value = uiState.query,
              onValueChange = { onSearchQueryChange(it) },
              modifier = Modifier.focusRequester(focusRequester),
              shape = RoundedCornerShape(99.dp),
              label = null,
              singleLine = true,
              placeholder = { Text("Search") },
              leadingIcon = {
                //  Back icon
                IconButton(
                  onClick = {
                    focusManager.clearFocus()
                    backOnClick()
                  },
                  modifier = Modifier.padding(start = 4.dp)
                ) {
                  Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                  )
                }
              },
              trailingIcon = {
                if (uiState.query.isNotEmpty()) {
                  //  Clear icon
                  IconButton(
                    onClick = { onSearchQueryChange("") },
                    modifier = Modifier.padding(end = 4.dp)
                  ) {
                    Icon(
                      imageVector = Icons.Rounded.Close,
                      contentDescription = "Clear",
                      tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                  }
                }
              })
          }

          // Filters
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Spacer(modifier = Modifier)

            InputChip(
              label = { Text(text = "All") },
              onClick = { onFilterChange(DiscoverSearchFilter.ALL) },
              selected = uiState.selectedFilter == DiscoverSearchFilter.ALL
            )

            InputChip(
              label = { Text(text = "Places") },
              onClick = { onFilterChange(DiscoverSearchFilter.PLACES) },
              selected = uiState.selectedFilter == DiscoverSearchFilter.PLACES
            )

            InputChip(
              label = { Text(text = "People") },
              onClick = { onFilterChange(DiscoverSearchFilter.PEOPLE) },
              selected = uiState.selectedFilter == DiscoverSearchFilter.PEOPLE
            )
          }

          Divider(modifier = Modifier.padding(top = 8.dp))
        }
      }
    },
  ) { innerPadding ->

    // Empty state
    if (uiState.resultPlaces.isEmpty() && uiState.resultUsers.isEmpty()) {
      Box(
        modifier = Modifier
          .padding(innerPadding)
          .fillMaxSize()
          .imePadding(),
        contentAlignment = Alignment.Center
      ) {
        val placeholderText = if (uiState.query.isBlank()) {
          ""
        } else {
          "No result"
        }
        Text(
          text = placeholderText,
          color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.8f),
          textAlign = TextAlign.Center
        )
      }
    }
    Column(
      modifier = Modifier
        .padding(top = innerPadding.calculateTopPadding())
        .imePadding()
    ) {

      when (uiState.selectedFilter) {

        // Filter All
        DiscoverSearchFilter.ALL    -> {

          // Places results
          Text(
            text = "Places",
            modifier = Modifier.padding(
              top = 16.dp,
              start = 16.dp
            ),
            style = MaterialTheme.typography.labelLarge,
          )
          when (uiState.resultPlaces.isNotEmpty()) {
            true  -> {
              Column {
                for (place in uiState.resultPlaces.subList(
                  0,
                  3.coerceAtMost(uiState.resultPlaces.size)
                )) {
                  ListItem(overlineContent = {},
                    leadingContent = {
                      Icon(
                        imageVector = Icons.Rounded.LocationOn,
                        contentDescription = "Place",
                      )
                    },
                    headlineContent = { Text(place.name) },
                    supportingContent = {
                      Text(text = place.address)
                    },
                    modifier = Modifier.clickable {
                      placeResultOnClick(place.id)
                    })
                }
              }
            }

            false -> {
              Box(
                modifier = Modifier
                  .padding(
                    top = 16.dp,
                    start = 16.dp
                  )
                  .fillMaxWidth()
              ) {
                Text(
                  text = "No places found",
                  style = MaterialTheme.typography.bodySmall,
                  textAlign = TextAlign.Center,
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                  color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.8f)
                )
              }
            }
          }

          // People results
          Text(
            text = "People",
            modifier = Modifier.padding(
              top = 16.dp,
              start = 16.dp
            ),
            style = MaterialTheme.typography.labelLarge,
          )
          when (uiState.resultUsers.isNotEmpty()) {
            true  -> {
              Column {
                for (user in uiState.resultUsers.subList(
                  0,
                  3.coerceAtMost(uiState.resultUsers.size)
                )) {
                  ListItem(overlineContent = {},
                    leadingContent = {
                      Image(
                        model = user.image,
                        contentDescription = "Notification image",
                        modifier = Modifier
                          .size(40.dp)
                          .clip(CircleShape),
                      )
                    },
                    headlineContent = { Text("@${user.username}") },
                    supportingContent = {
                      Text(text = user.name)
                    },
                    modifier = Modifier.clickable {
                      userResultOnClick(user.id)
                    })
                }
              }
            }

            false -> {
              Box(
                modifier = Modifier
                  .padding(
                    top = 16.dp,
                    start = 16.dp
                  )
                  .fillMaxWidth()
              ) {
                Text(
                  text = "No person found",
                  style = MaterialTheme.typography.bodySmall,
                  textAlign = TextAlign.Center,
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                  color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.8f)
                )
              }
            }
          }
        }

        DiscoverSearchFilter.PLACES -> {
          Text(
            text = "Places",
            modifier = Modifier.padding(
              top = 16.dp,
              start = 16.dp
            ),
            style = MaterialTheme.typography.labelLarge,
          )

          // Places results
          when (uiState.resultPlaces.isNotEmpty()) {
            true  -> {
              Column {
                for (place in uiState.resultPlaces.subList(
                  0,
                  3.coerceAtMost(uiState.resultPlaces.size)
                )) {
                  ListItem(overlineContent = {},
                    leadingContent = {
                      Icon(
                        imageVector = Icons.Rounded.LocationOn,
                        contentDescription = "Place",
                      )
                    },
                    headlineContent = { Text(place.name) },
                    supportingContent = {
                      Text(text = place.address)
                    },
                    modifier = Modifier.clickable {
                      placeResultOnClick(place.id)
                    })
                }
              }
            }

            false -> {
              Box(
                modifier = Modifier
                  .padding(
                    top = 16.dp,
                    start = 16.dp
                  )
                  .fillMaxWidth()
              ) {
                Text(
                  text = "No places found",
                  style = MaterialTheme.typography.bodySmall,
                  textAlign = TextAlign.Center,
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                  color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.8f)
                )
              }
            }
          }
        }

        DiscoverSearchFilter.PEOPLE -> {
          Text(
            text = "People",
            modifier = Modifier.padding(
              top = 16.dp,
              start = 16.dp
            ),
            style = MaterialTheme.typography.labelLarge,
          )

          // Users results
          when (uiState.resultUsers.isNotEmpty()) {
            true  -> {
              Column {
                for (user in uiState.resultUsers.subList(
                  0,
                  3.coerceAtMost(uiState.resultUsers.size)
                )) {
                  ListItem(overlineContent = {},
                    leadingContent = {
                      Image(
                        model = user.image,
                        contentDescription = "Notification image",
                        modifier = Modifier
                          .size(40.dp)
                          .clip(CircleShape),
                      )
                    },
                    headlineContent = { Text(user.username) },
                    supportingContent = {
                      Text(text = user.name)
                    },
                    modifier = Modifier.clickable {
                      userResultOnClick(user.id)
                    })
                }
              }
            }

            false -> {
              Box(
                modifier = Modifier
                  .padding(
                    top = 16.dp,
                    start = 16.dp
                  )
                  .fillMaxWidth()
              ) {
                Text(
                  text = "No person found",
                  style = MaterialTheme.typography.bodySmall,
                  textAlign = TextAlign.Center,
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                  color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.8f)
                )
              }
            }
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
private fun DiscoverSearchPreview(

) {
  LocalLensTheme {
    DiscoverSearch(
      uiState = DiscoverSearchUiState(
        resultPlaces = listOf(
          SampleData.samplePlace,
          SampleData.samplePlace1,
        ),
        resultUsers = listOf(
          SampleData.sampleUser,
          SampleData.sampleUser1,
          SampleData.sampleUser2,
          SampleData.sampleSuperUser
        ),
        isLoading = false,
      ),
    )
  }
}

@Preview(
  showBackground = true,
  showSystemUi = true,
  wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
  uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun DiscoverSearchEmptyPreview(

) {
  LocalLensTheme {
    DiscoverSearch(
      uiState = DiscoverSearchUiState(
        resultPlaces = listOf(

        ),
        resultUsers = listOf(

        ),
        isLoading = false,
      ),
    )
  }
}