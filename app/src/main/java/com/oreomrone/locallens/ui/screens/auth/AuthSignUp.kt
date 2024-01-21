package com.oreomrone.locallens.ui.screens.auth

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.oreomrone.locallens.ui.components.ErrorAwareOutlinedTextField
import com.oreomrone.locallens.ui.components.GoogleAuthButton
import com.oreomrone.locallens.ui.theme.LocalLensTheme
import kotlinx.coroutines.launch

@Composable
fun AuthSignUp(
  signInOnClick: () -> Unit = {},
) {
  val viewModel: AuthSignUpViewModel = hiltViewModel()
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  AuthSignUp(
    uiState = uiState,
    updateUiState = viewModel::updateUiState,
    googleAuthOnClick = viewModel::onSignUpWithGoogle,
    signUpOnClick = viewModel::onSignUp,
    signInOnClick = signInOnClick
  )
}

@OptIn(
  ExperimentalMaterial3Api::class,
  ExperimentalComposeUiApi::class
)
@Composable
private fun AuthSignUp(
  uiState: AuthSignUpUiState = AuthSignUpUiState(),
  updateUiState: (AuthSignUpInfo) -> Unit = {},
  googleAuthOnClick: suspend () -> Unit = {},
  signUpOnClick: suspend () -> Unit = {},
  signInOnClick: () -> Unit = {},
) {
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
  val coroutineScope = rememberCoroutineScope()
  val keyboardController = LocalSoftwareKeyboardController.current

  if (uiState.isLoading) {
    Surface(
      modifier = Modifier.fillMaxSize(),
      color = MaterialTheme.colorScheme.surface
    ) {
      Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
      ) {
        CircularProgressIndicator(
          modifier = Modifier.width(56.dp),
          color = MaterialTheme.colorScheme.secondary,
          trackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
        )
      }
    }
  }

  Scaffold(
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection).imePadding(),
    topBar = {
      CenterAlignedTopAppBar(
        title = {
          Text("Join LocalLens")
        })
    },
    snackbarHost = {
      SnackbarHost(
        hostState = uiState.snackbarHostState
      )
    },
  ) { innerPadding ->
    Column(
      Modifier
        .padding(top = innerPadding.calculateTopPadding())
        .padding(horizontal = 16.dp)
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
        .imePadding(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(
        space = 32.dp,
      ),
    ) {
      //  Google
      GoogleAuthButton(onClick = {
        coroutineScope.launch {
          googleAuthOnClick()
        }
      })

      Divider()

      Column(
        verticalArrangement = Arrangement.spacedBy(
          space = 12.dp,
          alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        ErrorAwareOutlinedTextField(
          value = uiState.authSignUpInfo.email,
          onValueChange = { updateUiState(uiState.authSignUpInfo.copy(email = it)) },
          label = { Text(text = "Email") },
          isError = uiState.emailValid.not(),
          supportingText = { Text("The entered email address is in the wrong format") },
          maxLines = 1,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        ErrorAwareOutlinedTextField(
          modifier = Modifier.padding(top = 12.dp),
          value = uiState.authSignUpInfo.password,
          onValueChange = { updateUiState(uiState.authSignUpInfo.copy(password = it)) },
          label = { Text("Password") },
          visualTransformation = PasswordVisualTransformation(),
          isError = uiState.passwordValid.not(),
          supportingText = { Text("Make sure the password is 8 characters long and includes a mix of lowercase and uppercase letters, numbers, and symbols") },
          maxLines = 1,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        )

        ErrorAwareOutlinedTextField(
          value = uiState.authSignUpInfo.passwordConfirm,
          onValueChange = { updateUiState(uiState.authSignUpInfo.copy(passwordConfirm = it)) },
          label = { Text("Confirm password") },
          isError = uiState.passwordConfirmValid.not(),
          maxLines = 1,
          supportingText = { Text("The password confirmation doesn't match") },
          visualTransformation = PasswordVisualTransformation(),
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        )
      }

      Column(
        verticalArrangement = Arrangement.spacedBy(
          space = 12.dp,
          alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally,


        ) {
        Button(
          enabled = (uiState.emailValid && uiState.passwordValid && uiState.passwordConfirmValid),
          onClick = {
            coroutineScope.launch {
              keyboardController?.hide()
              signUpOnClick()
            }
          },
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(text = "Sign up with email")
        }
      }

      Divider()

      Column(
        verticalArrangement = Arrangement.spacedBy(
          space = 12.dp,
          alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(
          bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding() + 16.dp
        )

      ) {
        Text(text = "Already have an account?")

        OutlinedButton(onClick = { signInOnClick() }) {
          Text(text = "Sign in")
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
private fun AuthSignUpPreview() {
  LocalLensTheme {
    AuthSignUp()
  }
}
