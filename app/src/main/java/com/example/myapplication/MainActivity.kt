package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.data.model.ErrorMessage
import com.example.myapplication.data.storage.DataStore
import com.example.myapplication.ui.signin.SignInRoute
import com.example.myapplication.ui.signin.SignInScreen
import com.example.myapplication.ui.signup.SignUpRoute
import com.example.myapplication.ui.signup.SignUpScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.welcome.WelcomeRoute
import com.example.myapplication.ui.welcome.WelcomeScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {


            val scope = rememberCoroutineScope()
            val snackbarHostState = remember { SnackbarHostState() }
            val navController = rememberNavController()
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = SignInRoute,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable<WelcomeRoute> {
                                BackHandler(true) { Log.i("RRR", "Clicked back") }
                                WelcomeScreen(
                                    openSignInScreen = {
                                        navController.navigate(SignInRoute) { launchSingleTop = true }
                                    },
                                    showErrorSnackbar = { errorMessage ->
                                        val message = getErrorMessage(errorMessage)
                                        scope.launch { snackbarHostState.showSnackbar(message) }
                                    }
                                )
                            }
                            composable<SignInRoute> {
                                BackHandler(true) { Log.i("RRR", "Clicked back") }
                                SignInScreen(
                                    openWelcomeScreen = {
                                        navController.navigate(WelcomeRoute) { launchSingleTop = true }
                                    },
                                    openSignUpScreen = {
                                        navController.navigate(SignUpRoute) { launchSingleTop = true }
                                    },
                                    showErrorSnackbar = { errorMessage ->
                                        val message = getErrorMessage(errorMessage)
                                        scope.launch { snackbarHostState.showSnackbar(message) }
                                    }
                                )
                            }
                            composable<SignUpRoute> { SignUpScreen(
                                openWelcomeScreen = {
                                    navController.navigate(WelcomeRoute) { launchSingleTop = true }
                                },
                                showErrorSnackbar = { errorMessage ->
                                    val message = getErrorMessage(errorMessage)
                                    scope.launch { snackbarHostState.showSnackbar(message) }
                                }
                            )}
                        }
                    }
                }
            }
        }
    }

    private fun getErrorMessage(error: ErrorMessage): String {
        return when (error) {
            is ErrorMessage.StringError -> error.message
            is ErrorMessage.IdError -> this@MainActivity.getString(error.message)
        }
    }
}