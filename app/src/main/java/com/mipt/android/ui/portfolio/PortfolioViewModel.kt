package com.mipt.android.ui.portfolio

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mipt.android.common.SingleLiveEvent
import com.mipt.android.data.TinkoffRepository
import com.mipt.android.data.api.responses.StockInfoResponse
import com.mipt.android.data.api.responses.portfolio.PortfolioResponse
import com.mipt.android.launchWithErrorHandler
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

    val toast: LiveData<String?>
        get() = _toast
    private val _toast = MutableLiveData<String?>()

    val result: LiveData<List<PortfolioResponse.PositionItem>>
        get() = _result
    private val _result = MutableLiveData<List<PortfolioResponse.PositionItem>>()

    private val _openDetailAction = SingleLiveEvent<PortfolioResponse.PositionItem>()
    val openDetailAction: LiveData<PortfolioResponse.PositionItem> = _openDetailAction

    init {
        viewModelScope.launchWithErrorHandler(block = {
            val accountID = sessionManager.getBrokerAccountId();

            val result = tinkoffRepository.getPortfolio(accountID);

            for (i in result.positions) {
                val candleArray = tinkoffRepository.getCandles(i.figi, "1min").candles
                i.ticker = "123" // candleArray.last().c.toString();
            }


            _result.postValue(result.positions);
        }, onError = {
            showToast("Неверный API токен")
        })
    }

    private fun showToast(message: String) {
        _toast.postValue(message)
    }

    fun onItemClicked(item: PortfolioResponse.PositionItem) {
        _openDetailAction.value = item

    }


}