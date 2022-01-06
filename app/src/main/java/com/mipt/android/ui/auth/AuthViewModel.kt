package com.mipt.android.ui.auth

import androidx.lifecycle.*
import com.mipt.android.data.TinkoffRepository
import com.mipt.android.di.TokenManager
import com.mipt.android.launchWithErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val tinkoffRepository: TinkoffRepository,
    private val tokenManager: TokenManager
) : ViewModel() {
    val token: LiveData<String?>
        get() = _token
    private val _token = MutableLiveData<String>()

    val error: LiveData<String?>
        get() = _error
    private val _error = MutableLiveData<String?>()

    init {
        _token.postValue(tokenManager.getToken())
    }

    fun onApply(token: String) {
        if (isValidToken(token)) {
            _token.postValue(token)
            tokenManager.saveToken(token)

            viewModelScope.launchWithErrorHandler(block = {
                val response = tinkoffRepository.registerAccount()
                _error.postValue("id: ${response.brokerAccountId}")
            }, onError = {
                showError()
            })

        } else {
            showError()
        }
    }

    private fun isValidToken(token: String): Boolean {
        return token.length >= 20
    }

    private fun showError() {
        _error.postValue("Неверный токен")
    }
}