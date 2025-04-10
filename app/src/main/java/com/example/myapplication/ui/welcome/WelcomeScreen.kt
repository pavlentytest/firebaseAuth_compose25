package com.example.myapplication.ui.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.serialization.Serializable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.myapplication.R
import com.example.myapplication.data.injection.FirebaseHiltModule.auth
import com.example.myapplication.data.model.ErrorMessage
import com.example.myapplication.data.storage.DataStore
import com.example.myapplication.ui.signup.SignUpScreenContent

@Serializable
object WelcomeRoute

@Composable
fun WelcomeScreen(openSignInScreen: () -> Unit,
                  showErrorSnackbar: (ErrorMessage) -> Unit,
                  viewModel: WelcomeViewModel = hiltViewModel()) {
    val shouldRestartApp by viewModel.shouldRestartApp.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val store = DataStore(context)
    val dataText = store.getData.collectAsState(initial = "")

    if (shouldRestartApp) {
        openSignInScreen()
    } else {
        WelcomeScreenContent(
            data = dataText.toString(),
            signOut = viewModel::signOut,
            showErrorSnackbar = showErrorSnackbar
        )
    }
}
@Composable
fun WelcomeScreenContent(
    data: String,
    signOut: () -> Unit,
    showErrorSnackbar: (ErrorMessage) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(stringResource(R.string.user, data))
        Button(onClick = { signOut() }) {
            Text(stringResource(R.string.sign_out))
        }
    }

}

