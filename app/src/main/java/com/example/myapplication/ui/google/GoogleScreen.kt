package com.example.myapplication.ui.google

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.google.GoogleViewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import android.content.Context
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.R
import com.example.myapplication.ui.signin.SignInScreenContent
import com.example.myapplication.ui.signin.SignInViewModel
import kotlinx.serialization.Serializable

@Composable
fun SignInWithGoogleButton(
    openWelcomeScreen: () -> Unit,
    googleViewModel: GoogleViewModel = hiltViewModel()
) {
    val shouldRestartApp by googleViewModel.shouldRestartApp.collectAsStateWithLifecycle()
    if (shouldRestartApp) {
       openWelcomeScreen()
    } else {
        val coroutineScope = rememberCoroutineScope()
        val context = LocalContext.current
        OutlinedButton(
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            onClick = {
                doGoogleSignIn(
                    googleViewModel,
                    coroutineScope,
                    context,
                )
            }
        ) {
            Text(
                text = "Sign in with Google",
            )
        }
    }
}

private fun doGoogleSignIn(
    googleViewModel: GoogleViewModel,
    coroutineScope: CoroutineScope,
    context: Context,
) {
    val credentialManager = CredentialManager.create(context)
    fun getSignInWithGoogleOption(context: Context): GetGoogleIdOption {
        return GetGoogleIdOption.Builder()
            .setServerClientId(context.getString(R.string.web_client_id))
            .setFilterByAuthorizedAccounts(false)
            .build()
    }
    coroutineScope.launch {
        try {
            val googleSignRequest: GetCredentialRequest = GetCredentialRequest.Builder()
                .addCredentialOption(getSignInWithGoogleOption(context))
                .build()

            val result = credentialManager.getCredential(
                request = googleSignRequest,
                context = context,
            )
            googleViewModel.signInGoogle(result.credential.data.toString())
            Log.d("RRR",result.credential.data.toString())
        } catch (e: Exception) {
            Log.e("RRR",e.message.toString())
        } catch (e: GetCredentialException) {
            e.printStackTrace()
        }
    }
}
