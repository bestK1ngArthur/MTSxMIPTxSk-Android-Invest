package com.mipt.android.ui.portfolio

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mipt.android.R
import com.mipt.android.data.TinkoffRepository
import com.mipt.android.preferences.SessionManager
import com.mipt.android.preferences.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class PortfolioViewModel @Inject constructor(
    private val tinkoffRepository: TinkoffRepository,
    private val tokenManager: TokenManager,
    private val sessionManager: SessionManager,
    @ApplicationContext private val context: Context
) : ViewModel() {
    val balance: LiveData<String?>
        get() = _balance
    private val _balance = MutableLiveData<String?>()

    private val _token = MutableLiveData<String?>()

    private val _accountID = MutableLiveData<String?>()

    val toast: LiveData<String?>
        get() = _toast
    private val _toast = MutableLiveData<String?>()

    init {
        _token.postValue(tokenManager.getToken())

        val accountIDValue = sessionManager.getBrokerAccountId()
        if (accountIDValue != null) {
            _accountID.postValue(accountIDValue)
        }

        getListItem()
    }

    private fun getListItem() {

    }

    private fun showToast(message: String) {
        _toast.postValue(message)
    }
}