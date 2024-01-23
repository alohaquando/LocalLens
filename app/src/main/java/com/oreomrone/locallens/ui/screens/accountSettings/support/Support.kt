package com.oreomrone.locallens.ui.screens.accountSettings.support

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oreomrone.locallens.ui.components.BottomButtonBar
import com.oreomrone.locallens.ui.theme.LocalLensTheme

@Composable
fun Support(
  backOnClick: () -> Unit = {},
) {
  val viewModel: SupportViewModel = hiltViewModel()
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  Support(
    uiState = uiState,
    onTypeSelected = viewModel::onTypeSelected,
    onContentChanged = viewModel::onContentChanged,
    onSubmitClicked = viewModel::performSubmit,
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Support(
  uiState: SupportUiState = SupportUiState(),
  onBackClicked: () -> Unit = {},
  onTypeSelected: (SupportType) -> Unit = {},
  onContentChanged: (String) -> Unit = {},
  onSubmitClicked: () -> Unit = {},
) {

  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

  Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection).imePadding(),
    topBar = {
      CenterAlignedTopAppBar(
        title = { Text(text = "Support") },
        navigationIcon = {
          IconButton(onClick = onBackClicked) {
            Icon(
              imageVector = Icons.Default.ArrowBack,
              contentDescription = "Back",
            )
          }
        },
        scrollBehavior = scrollBehavior
      )
    },
    bottomBar = {
      BottomButtonBar(
        text = "Submit",
        onClick = onSubmitClicked,
        enabled = uiState.isInputValid
      )
    }


  ) { it ->
    Column(
      modifier = Modifier
        .verticalScroll(rememberScrollState())
        .padding(top = it.calculateTopPadding())
        .imePadding()
    ) {

      // Choices
      Text(
        text = "You're reaching out about",
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier.padding(
          horizontal = 16.dp,
          vertical = 8.dp
        )
      )
      ListItem(headlineContent = { Text(text = "Question") },
        supportingContent = { Text(text = "How do I...") },
        trailingContent = {
          RadioButton(selected = uiState.type == SupportType.QUESTION,
            onClick = { onTypeSelected(SupportType.QUESTION) })
        },
        modifier = Modifier.clickable {
          onTypeSelected(SupportType.QUESTION)
        })
      ListItem(headlineContent = { Text(text = "Feedback") },
        supportingContent = { Text(text = "What if we...") },
        trailingContent = {
          RadioButton(selected = uiState.type == SupportType.FEEDBACK,
            onClick = { onTypeSelected(SupportType.FEEDBACK) })
        },
        modifier = Modifier.clickable {
          onTypeSelected(SupportType.FEEDBACK)
        })
      ListItem(headlineContent = { Text(text = "Bug") },
        supportingContent = { Text(text = "When I try...") },
        trailingContent = {
          RadioButton(selected = uiState.type == SupportType.BUG,
            onClick = { onTypeSelected(SupportType.BUG) })
        },
        modifier = Modifier.clickable {
          onTypeSelected(SupportType.BUG)
        })

      Divider(
        modifier = Modifier.padding(
          horizontal = 16.dp,
          vertical = 8.dp
        )
      )

      // Content
      Text(
        text = "Please provide as much detail as you can about the issue",
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier.padding(
          horizontal = 16.dp,
          vertical = 16.dp
        )
      )
      OutlinedTextField(
        value = uiState.content,
        onValueChange = { onContentChanged(it) },
        placeholder = { Text(text = "Details") },
        modifier = Modifier
          .fillMaxWidth()
          .height(240.dp)
          .padding(
            horizontal = 16.dp,
          )
      )

      Spacer(modifier = Modifier.height(it.calculateBottomPadding()))
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
private fun SupportPreview(

) {
  LocalLensTheme {
    Support(
      uiState = SupportUiState(
        type = SupportType.QUESTION,
        content = "How do I...",
        isInputValid = false
      )
    )
  }
}