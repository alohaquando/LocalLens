package com.oreomrone.locallens.ui.components.layouts

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.oreomrone.locallens.ui.components.BottomTextMessageBar
import com.oreomrone.locallens.ui.theme.LocalLensTheme
import com.oreomrone.locallens.ui.utils.conditional
import kotlin.random.Random

@Composable
fun Message(
  text: String,
  byCurrentUser: Boolean,
  timeStamp: String,
) {
  val configuration = LocalConfiguration.current
  val screenWidth = configuration.screenWidthDp.dp
  val messageOffset = screenWidth / 8

  var showTimeStamp by remember {
    mutableStateOf(false)
  }

  Row(
    modifier = Modifier.fillMaxSize(),
    horizontalArrangement = if (byCurrentUser) Arrangement.End else Arrangement.Start
  ) {
    Surface(color = if (byCurrentUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
      shape = RoundedCornerShape(24.dp),
      modifier = Modifier
        .conditional(
          byCurrentUser
        ) {
          padding(start = messageOffset)
        }
        .conditional(
          !byCurrentUser
        ) {
          padding(end = messageOffset)
        }
        .clip(RoundedCornerShape(24.dp))
        .clickable {
          showTimeStamp = !showTimeStamp
        }) {
      Column(
        modifier = Modifier.padding(
          horizontal = 16.dp,
          vertical = 8.dp,
        ),
        horizontalAlignment = Alignment.Start
      ) {
        Text(
          text = text,
          color = if (byCurrentUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
        )
        AnimatedContent(
          targetState = showTimeStamp,
          label = "Show text message timestamp"
        ) { targetState ->
          if (targetState) {
            Text(
              text = timeStamp,
              color = if (byCurrentUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
              style = MaterialTheme.typography.labelSmall,
              modifier = Modifier
                .alpha(0.5f)
                .padding(vertical = 4.dp)
            )
          }
        }
      }
    }
  }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(
  showBackground = true,
  showSystemUi = true,
  wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
  uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun MessagePreview(

) {
  val messages: List<String> = listOf(
    "Hey there! How's your day going?",
    "What's up? Any exciting plans for the weekend?",
    "Just wanted to say hi and check in. How are you?",
    "In the tapestry of life, our friendship is a vibrant thread that adds color to the canvas.",
    "I appreciate the genuine connection we share and the way you make every moment memorable.",
    "Here's to the beautiful journey we've embarked on together – may it continue to flourish and thrive.",
    "Guess what? ",
    "I can't believe it's already Monday again. Time flies!",
    "Thinking about trying a new recipe for dinner tonight. Any suggestions?",
    "Did you hear the latest news? It's pretty interesting!",
    "Feeling a bit stressed lately. Any tips for relaxation?",
    "Planning a movie night this Friday. Any movie recommendations?",
    "Wishing you a fantastic day ahead! 😊",
    "I wanted to take a moment to express my gratitude for your friendship.",
    "Your support and kindness mean the world to me, and I cherish the memories we've created together.",
    "Looking forward to many more adventures and laughter in the days to come."
  )

  LocalLensTheme {
    Scaffold(topBar = {
      CenterAlignedTopAppBar(title = {
        Text(text = "Messages")
      },
        navigationIcon = {
          IconButton(onClick = { }) {
            Icon(
              imageVector = Icons.Rounded.ArrowBack,
              contentDescription = "Back",
            )
          }
        })
    },
      bottomBar = {
        BottomTextMessageBar()
      }) { innerPadding ->
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 16.dp),
        contentPadding = innerPadding,
        reverseLayout = true,
        verticalArrangement = Arrangement.spacedBy(
          8.dp,
          alignment = Alignment.Bottom
        )
      ) {
        for (message in messages) {
          item {
            Message(
              text = message,
              byCurrentUser = Random.nextBoolean(),
              timeStamp = "12:00"
            )
          }
        }
      }
    }
  }
}