package com.oreomrone.locallens.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.oreomrone.locallens.domain.Place

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceSearchSheet(
  onDismissRequest: () -> Unit = { },
  placeSearchQuery: String,
  onPlaceSearchQueryChange: (String) -> Unit,
  onPlaceSearchEnter: () -> Unit,
  placeSearchResult: List<Place>,
  placeResultOnClick: (Place) -> Unit,
) {
  ModalBottomSheet(
    sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    onDismissRequest = onDismissRequest,
    dragHandle = {},
    windowInsets = WindowInsets.ime,
  ) {
    val sheetScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
      modifier = Modifier.nestedScroll(sheetScrollBehavior.nestedScrollConnection),
      topBar = {
        Surface(color = MaterialTheme.colorScheme.surface) {
          Column {
            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .padding(
                  top = WindowInsets.systemBars
                    .asPaddingValues()
                    .calculateTopPadding(),
                )
                .padding(
                  horizontal = 16.dp,
                  vertical = 8.dp
                )
                .height(56.dp)
            ) {
              OutlinedTextField(value = placeSearchQuery,
                onValueChange = { onPlaceSearchQueryChange(it) },
                shape = RoundedCornerShape(99.dp),
                label = null,
                singleLine = true,
                keyboardActions = KeyboardActions(onDone = { onPlaceSearchEnter() }),
                placeholder = { Text("Find a place by name or address") },
                leadingIcon = {
                  //  Back icon
                  IconButton(
                    onClick = {
                      onDismissRequest()
                    },
                    modifier = Modifier.padding(start = 4.dp)
                  ) {
                    Icon(
                      imageVector = Icons.Rounded.KeyboardArrowDown,
                      contentDescription = "Close sheet",
                      tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                  }
                },
                trailingIcon = {
                  if (placeSearchQuery.isNotEmpty()) {
                    //  Clear icon
                    IconButton(
                      onClick = { onPlaceSearchQueryChange("") },
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
            Divider(modifier = Modifier.padding(top = 8.dp))
          }
        }
      },
    ) { innerPadding ->
      Column(
        modifier = Modifier
          .verticalScroll(rememberScrollState())
          .padding(top = innerPadding.calculateTopPadding())
          .imePadding()
      ) {
        when {
          placeSearchResult.isNotEmpty()                               -> {
            Column {
              for (place in placeSearchResult) {
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
                    placeResultOnClick(place)
                    onDismissRequest()
                  })
              }
            }
          }

          placeSearchQuery.isNotEmpty()                                -> {
            Box(
              modifier = Modifier
                .padding(
                  top = 16.dp,
                  start = 16.dp
                )
                .fillMaxWidth()
            ) {
              Text(
                text = "Press \"Enter\" on your keyboard to search",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.8f)
              )
            }
          }

          placeSearchResult.isEmpty() && placeSearchQuery.isNotEmpty() -> {
            Box(
              modifier = Modifier
                .padding(
                  top = 16.dp,
                  start = 16.dp
                )
                .fillMaxWidth()
            ) {
              Text(
                text = "No results found",
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