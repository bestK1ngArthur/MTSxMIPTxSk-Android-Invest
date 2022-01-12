package com.mipt.android.ui.auth

import android.content.Context
import androidx.lifecycle.*
import com.mipt.android.R
import com.mipt.android.data.TinkoffRepository
import com.mipt.android.data.api.responses.CandlesResponse
import com.mipt.android.data.api.responses.UserAccountsResponse
import com.mipt.android.preferences.TokenManager
import com.mipt.android.launchWithErrorHandler
import com.mipt.android.preferences.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val tinkoffRepository: TinkoffRepository,
    private val tokenManager: TokenManager,
    private val sessionManager: SessionManager,
    @ApplicationContext private val context: Context
) : ViewModel() {
    val token: LiveData<String?>
        get() = _token
    private val _token = MutableLiveData<String?>()

    val accountID: LiveData<String?>
        get() = _accountID
    private var _accountID = MutableLiveData<String?>()

    val startDate: LiveData<String?>
        get() = _startDate
    private var _startDate = MutableLiveData<String?>()

    val buttonText: LiveData<String>
        get() = _buttonText
    private val _buttonText = MutableLiveData<String>()

    val toast: LiveData<String?>
        get() = _toast
    private val _toast = MutableLiveData<String?>()

    val isLoading: LiveData<Boolean>
        get() = _isLoading
    private val _isLoading = MutableLiveData<Boolean>()

    val isPortfolioShown: LiveData<Boolean>
        get() = _isPortfolioShown
    private val _isPortfolioShown = MutableLiveData<Boolean>()

    val isCloseButtonShown: LiveData<Boolean>
        get() = accountID.map { it != null }

    init {
        _token.postValue(tokenManager.getToken())
        _accountID.postValue(sessionManager.getBrokerAccountId())
        _startDate.postValue(sessionManager.getCandles())
        print("WHERHEHRHE")
        print(_accountID)


        if (tokenManager.getToken() != null && sessionManager.isSessionExists()) {
            _buttonText.postValue(context.getString(R.string.logout))
        } else {
            _buttonText.postValue(context.getString(R.string.login))
        }

        _isLoading.postValue(false)
        _isPortfolioShown.postValue(false)
    }

    fun onTap(token: String) {
        if (sessionManager.isSessionExists()) {
            logout()
        } else {
            login(token)
        }
    }

    private fun logout() {
        viewModelScope.launchWithErrorHandler(block = {
            _isLoading.postValue(true)

            // If need remove account on logout
            // tinkoffRepository.removeAccount(sessionManager.getBrokerAccountId() ?: "")

            sessionManager.removeSession()
            _accountID.postValue(null)
            _startDate.postValue(null)

            tokenManager.removeToken()
            _token.postValue(null)

            _buttonText.postValue(context.getString(R.string.login))

            _isLoading.postValue(false)
        }, onError = {
            _isLoading.postValue(false)
            showToast("Не получилось выйти из аккаунта :(")
        })
    }

    private fun login(tokenValue: String) {
        _token.postValue(tokenValue)

        if (!isValidToken(tokenValue)) {
            showToast("Неверный API токен")
        }

        viewModelScope.launchWithErrorHandler(block = {
            _isLoading.postValue(true)

            if (tokenValue != null) {
                tokenManager.saveToken(tokenValue)
            }

            var userAccount = tinkoffRepository.getUserAccounts().accounts.firstOrNull()
            var candles = tinkoffRepository.getCandles("BBG005DXJS36").candles.firstOrNull()

            if (userAccount == null) {
                val response = tinkoffRepository.registerAccount()
                userAccount = UserAccountsResponse.Account(response.brokerAccountType, response.brokerAccountId)
            }
            var figi = candles!!.figi
            if (candles == null) {
                figi = "BBG005DXJS36"

            }

            sessionManager.createSession(userAccount.brokerAccountId, figi)
            _accountID.postValue(userAccount.brokerAccountId)
            _startDate.postValue(candles.time)

            _buttonText.postValue(context.getString(R.string.logout))

            _isLoading.postValue(false)
            _isPortfolioShown.postValue(true)
        }, onError = {
            _isLoading.postValue(false)
            showToast("Неверный API токен")
        })
    }

    private fun isValidToken(token: String?): Boolean {
        return (token != null) && (token.length >= 20)
    }

    private fun showToast(message: String) {
        _toast.postValue(message)
    }
}