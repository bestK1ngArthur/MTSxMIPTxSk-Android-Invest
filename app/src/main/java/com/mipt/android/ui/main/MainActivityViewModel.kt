package com.mipt.android.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mipt.android.preferences.SessionManager
import com.mipt.android.preferences.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val sessionManager: SessionManager
) : ViewModel() {
    val isAuthorized: Boolean = tokenManager.getToken() != null && sessionManager.isSessionExists()
}