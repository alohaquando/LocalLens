package com.oreomrone.locallens.ui.tests

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.oreomrone.locallens.ui.components.Post


@Composable
fun TestScreen(
  viewModel: TestViewModel = hiltViewModel()
) {
  Post(postId = "ac613468-0ee9-4964-b833-9c18ac238da4")
}