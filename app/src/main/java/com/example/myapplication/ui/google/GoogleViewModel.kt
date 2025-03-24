package com.example.myapplication.ui.google

import com.example.myapplication.MainViewModel
import com.example.myapplication.data.model.ErrorMessage
import com.example.myapplication.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class GoogleViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : MainViewModel() {
    private val _shouldRestartApp = MutableStateFlow(false)
    val shouldRestartApp: StateFlow<Boolean>
        get() = _shouldRestartApp.asStateFlow()
    private val _userData = MutableStateFlow("Empty")
    val userData: StateFlow<String>
        get() = _userData.asStateFlow()

    fun signInGoogle(data: String) {
        _shouldRestartApp.value = true
        _userData.value = data
    }
}

