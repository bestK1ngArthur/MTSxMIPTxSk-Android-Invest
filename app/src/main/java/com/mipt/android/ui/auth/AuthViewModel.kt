package com.mipt.android.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AuthViewModel : ViewModel() {
    class Factory() : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AuthViewModel() as T
        }
    }

    val token: LiveData<String>
        get() = _token

    val error: LiveData<String>
        get() = _error

    private val _token = MutableLiveData<String>()
    private val _error = MutableLiveData<String>()

    fun onApply() {
        print("APPLY")
    }
}