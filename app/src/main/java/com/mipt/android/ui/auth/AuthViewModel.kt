package com.mipt.android.ui.auth

import androidx.lifecycle.*
import com.mipt.android.data.TinkoffRepository
import com.mipt.android.launchWithErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val tinkoffRepository: TinkoffRepository
) : ViewModel() {

    init {
        viewModelScope.launchWithErrorHandler(block = {
            tinkoffRepository.registerAccount()
            print("")
        }, onError = {
            print("")
        })
    }

    val error: LiveData<String?>
        get() = _error

    private val _token = MutableLiveData<String>()
    private val _error = MutableLiveData<String?>()

    fun onApply(token: String) {
        if (isValidToken(token)) {
            _token.postValue(token)
        } else {
            _error.postValue("Неверный токен")
        }
    }

    private fun isValidToken(token: String): Boolean {
        return token.isNotEmpty()
    }
}