package com.oreomrone.locallens.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ErrorAwareOutlinedTextField(
  modifier: Modifier = Modifier,
  value: String = "",
  onValueChange: (String) -> Unit = {},
  leadingIcon: @Composable (() -> Unit)? = null,
  label: @Composable (() -> Unit)? = null,
  placeholder: @Composable (() -> Unit)? = null,
  supportingText: @Composable (() -> Unit)? = null,
  isError: Boolean = false,
  maxLines: Int = 1,
  visualTransformation: VisualTransformation = VisualTransformation.None,
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,

) {
  var initialFocus by remember {
    mutableStateOf(false)
  }

  var canShowError by remember { mutableStateOf(false) }

  OutlinedTextField(modifier = Modifier
    .fillMaxWidth()
    .onFocusChanged {
      if (it.isFocused) {
        initialFocus = true
      }
      if (initialFocus && !it.isFocused) {
        canShowError = true
      }
    }
    .then(modifier),
    leadingIcon = leadingIcon,
    value = value,
    onValueChange = onValueChange,
    label = { label?.invoke() },
    isError = isError && canShowError,
    supportingText = {
      if (isError && canShowError) {
        supportingText?.invoke()
      }
    },
    placeholder = { placeholder?.invoke() },
    visualTransformation = visualTransformation,
    maxLines = maxLines,
    keyboardOptions = keyboardOptions,

  )
}

@Preview(
  showBackground = true,
  showSystemUi = true
)
@Composable
fun ErrorAwareOutlinedTextFieldPreview() {
  var value by remember {
    mutableStateOf("")
  }

  Column {
    ErrorAwareOutlinedTextField(
      value = value,
      onValueChange = { value = it },
      label = { Text(text = "Label") },
      placeholder = { Text(text = "Placeholder") },
      supportingText = { Text(text = "Supporting Text") },
      isError = true
    )

    ErrorAwareOutlinedTextField(
      value = value,
      onValueChange = { value = it },
      label = { Text(text = "Label") },
      placeholder = { Text(text = "Placeholder") },
      supportingText = { Text(text = "Supporting Text") },
      isError = true
    )
  }
}