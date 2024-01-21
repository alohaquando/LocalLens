package com.oreomrone.locallens.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Navigation
import androidx.compose.material.icons.rounded.ArrowCircleUp
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oreomrone.locallens.ui.theme.LocalLensTheme
import com.oreomrone.locallens.ui.utils.conditional

@Composable
fun Post(
  place: String = "Place Name",
  address: String = "Address",
  caption: String = "Caption",
  username: String = "Username",
  date: String = "Date",
  favorites: Int = 0,
  postImageModel: Any? = null,
  userImageModel: Any? = null,
  isFavorite: Boolean = false,
  showDivider: Boolean = true,
  showUser: Boolean = true,
  showMenuButton: Boolean = false,
  navigateOnClick: () -> Unit = {},
  favoriteOnClick: () -> Unit = {},
  placeOnClick: () -> Unit = {},
  userOnClick: () -> Unit = {},
  promoteOnClick: () -> Unit = {},
  editOnClick: () -> Unit = {},
  deleteOnClick: () -> Unit = {},
) {
  var captionIsOverflowing by remember {
    mutableStateOf(false)
  }

  var expandCaption by remember {
    mutableStateOf(false)
  }

  var captionLineCount by remember {
    mutableIntStateOf(0)
  }

  var expandMenu by remember { mutableStateOf(false) }

  var showDeleteDialog by remember { mutableStateOf(false) }

  Column(modifier = Modifier.padding(top = 8.dp)) {
    Image(
      modifier = Modifier.fillMaxWidth(),
      boxModifier = Modifier
        .padding(horizontal = 16.dp)
        .padding(
          top = 16.dp,
          bottom = 8.dp
        )
        .aspectRatio(1f)
        .clip(RoundedCornerShape(16.dp)),
      model = postImageModel
    )


    Column {
      Row {
        Column(
          modifier = Modifier
            .weight(1f)
            .clickable(onClick = placeOnClick)
            .padding(vertical = 8.dp)
            .padding(
              start = 16.dp,
              end = 8.dp
            ),
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Text(
            text = place,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
          )
          Text(
            text = address,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }
        Row(modifier = Modifier
          .conditional(showMenuButton) {
            weight(0.8f)
          }
          .conditional(!showMenuButton) {
            weight(0.5f)
          }
          .padding(end = 16.dp)
          .padding(top = 4.dp),
          horizontalArrangement = Arrangement.spacedBy(
            8.dp,
            Alignment.End
          )) {
          IconButton(
            onClick = navigateOnClick,
            colors = IconButtonDefaults.outlinedIconButtonColors(contentColor = MaterialTheme.colorScheme.primary)
          ) {
            Icon(
              imageVector = Icons.Outlined.Navigation,
              contentDescription = "Navigate to $place",
            )
          }
          IconButton(
            onClick = favoriteOnClick,

            colors = IconButtonDefaults.outlinedIconButtonColors(contentColor = if (isFavorite.not()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
          ) {
            Icon(
              imageVector = if (isFavorite.not()) {
                Icons.Rounded.FavoriteBorder
              } else {
                Icons.Rounded.Favorite
              },
              contentDescription = "Toggle favorite of this post",
            )
          }

          if (showMenuButton) {
            Box {
              IconButton(
                onClick = { expandMenu = true },
                colors = IconButtonDefaults.outlinedIconButtonColors(contentColor = MaterialTheme.colorScheme.onSurfaceVariant)
              ) {
                Icon(
                  imageVector = Icons.Rounded.MoreVert,
                  contentDescription = "More actions",
                )
              }
              DropdownMenu(
                expanded = expandMenu,
                onDismissRequest = { expandMenu = false },
                offset = DpOffset(
                  x = 16.dp,
                  y = 0.dp
                )
              ) {
                DropdownMenuItem(text = { Text(text = "Promote") },
                  leadingIcon = {
                    Icon(
                      imageVector = Icons.Rounded.Star,
                      contentDescription = "Promote"
                    )
                  },
                  onClick = { promoteOnClick() })
                Divider()
                DropdownMenuItem(text = { Text(text = "Edit") },
                  leadingIcon = {
                    Icon(
                      imageVector = Icons.Rounded.Edit,
                      contentDescription = "Edit"
                    )
                  },
                  onClick = { editOnClick() })
                DropdownMenuItem(
                  text = { Text(text = "Delete") },
                  leadingIcon = {
                    Icon(
                      imageVector = Icons.Rounded.Delete,
                      contentDescription = "Delete"
                    )
                  },
                  onClick = {
                    expandMenu = false
                    showDeleteDialog = true
                  },
                  colors = MenuDefaults.itemColors(
                    textColor = MaterialTheme.colorScheme.error,
                    leadingIconColor = MaterialTheme.colorScheme.error
                  )
                )
              }
            }
          }
        }
      }

      Column(modifier = Modifier
        .clickable(enabled = captionLineCount >= 3) {
          expandCaption = !expandCaption
        }
        .animateContentSize()
        .padding(vertical = 4.dp)
        .padding(horizontal = 16.dp)) {
        Text(text = caption,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = if (expandCaption) Int.MAX_VALUE else 3,
          overflow = if (expandCaption) TextOverflow.Visible else TextOverflow.Ellipsis,
          onTextLayout = {
            captionIsOverflowing = it.hasVisualOverflow
            captionLineCount = it.lineCount
          })


        if (captionLineCount >= 3) {
          if (captionIsOverflowing && !expandCaption) {
            Text(
              text = "See more",
              style = MaterialTheme.typography.titleMedium
            )
          } else {
            Text(
              text = "See less",
              style = MaterialTheme.typography.titleMedium
            )
          }
        }
      }

      Spacer(modifier = Modifier.size(4.dp))

      Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = date,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
          )
        }

        Icon(
          imageVector = Icons.Rounded.Circle,
          contentDescription = "Circle",
          modifier = Modifier.size(4.dp),
          tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)

        ) {
          Icon(
            imageVector = Icons.Rounded.FavoriteBorder,
            contentDescription = "Favorite Border",
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
          )
          Text(
            text = favorites.toString(),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
          )
          Text(
            text = "fav" + if (favorites != 1) "s" else "",
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
          )
        }
      }

      Spacer(modifier = Modifier.size(4.dp))

      if (showUser) {
        Row(modifier = Modifier
          .clickable {
            userOnClick()
          }
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
          .padding(vertical = 16.dp),
          verticalAlignment = Alignment.Top,
          horizontalArrangement = Arrangement.spacedBy(12.dp)) {
          Image(
            modifier = Modifier
              .size(24.dp)
              .aspectRatio(1f)
              .clip(CircleShape),
            model = userImageModel,
            onClick = userOnClick
          )
          Text(
            text = "@${username}",
            style = MaterialTheme.typography.titleMedium,
            lineHeight = 0.sp
          )
        }
      }

      Spacer(modifier = Modifier.size(32.dp))

      if (showDivider) {
        Divider(
          modifier = Modifier.padding(horizontal = 16.dp),
          color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
        )
      }
    }
  }

  if (showDeleteDialog) {
    AlertDialog(icon = {
      Icon(
        Icons.Rounded.Delete,
        contentDescription = "Delete"
      )
    },
      title = {
        Text(text = "Delete this post?")
      },
      text = {
        Text(text = "This post will permanently be deleted. This cannot be undone.")
      },
      onDismissRequest = {
        showDeleteDialog = false
      },
      confirmButton = {
        TextButton(
          onClick = {
            deleteOnClick()
            showDeleteDialog = false
          },
          colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
        ) {
          Text("Delete ")
        }
      },
      dismissButton = {
        TextButton(onClick = {
          showDeleteDialog = false
        }) {
          Text("Keep")
        }
      })
  }
}

@Preview(showBackground = true)
@Composable
private fun PostPreview() {
  LocalLensTheme {
    Post(
      place = "Post Office Square Park, Memorial Monument",
      address = "130 Congress St, Boston, MA 02110, United States",
      caption = "Captured one of my favorite places on a beautiful Sunday morning. Nothing better than this. I love being here so much, as it is so peaceful and beautiful. I love the architecture of the buildings. Honestly, I think this is one of the best places in Boston. There's so much to do here, and it's so interesting.",
      isFavorite = true,
      favorites = 59659,
      username = "oreomrone_the_explorer",
      date = "26 Sep 2021 10:00 AM",
      postImageModel = "https://images.unsplash.com/photo-1632835034837-4b8b9b5b9b9f?ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHw0Mnx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=60",
      userImageModel = "https://images.unsplash.com/photo-1632835034837-4b8b9b5b9b9f?ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHw0Mnx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=60"

    )
  }
}

@Preview(showBackground = true)
@Composable
fun PostPreviewShort() {
  LocalLensTheme {
    Post(
      place = "Post Office Square Park, Memorial Monument",
      address = "130 Congress St, Boston, MA 02110, United States",
      caption = "Captured one of my favorite places on a beautiful Sunday morning. ",
      isFavorite = false,
      showMenuButton = true,
      favorites = 59659,
      username = "oreomrone_the_explorer",
      date = "26 Sep 2021 10:00 AM",
      postImageModel = "https://images.unsplash.com/photo-1632835034837-4b8b9b5b9b9f?ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHw0Mnx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=60",
      userImageModel = "https://images.unsplash.com/photo-1632835034837-4b8b9b5b9b9f?ixid=MnwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHw0Mnx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=60"

    )
  }
}