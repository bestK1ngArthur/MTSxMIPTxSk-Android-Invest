package com.mipt.android.ui.portfolio

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mipt.android.common.SingleLiveEvent
import com.mipt.android.data.TinkoffRepository
import com.mipt.android.data.api.responses.portfolio.PortfolioResponse
import com.mipt.android.launchWithErrorHandler
import com.mipt.android.preferences.SessionManager
import com.mipt.android.preferences.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.math.roundToInt

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
            val accountID = sessionManager.getBrokerAccountId()

            val result = tinkoffRepository.getPortfolio(accountID)

            for (i in result.positions) {
                val Last = getLastPrice(i.figi)
                val cur = getCurrency(i.figi)
                val x = ((Last * i.balance.toDouble()) * 100).roundToInt() / 100.0
                i.blocked = Last.toString()
                i.ticker = "$x $cur"
            }


            _result.postValue(result.positions)
        }, onError = {
            showToast("???????????????? API ??????????")
        })
    }

    private fun showToast(message: String) {
        _toast.postValue(message)
    }

    fun onItemClicked(item: PortfolioResponse.PositionItem) {
        _openDetailAction.value = item
    }

    private suspend fun getLastPrice(figi: String): Double {
        val lastPrice = tinkoffRepository.getCandles(
            figi, "month", DateTimeFormatter.ISO_INSTANT.format(
                Instant.now().minus(30, ChronoUnit.DAYS)
            )
        ).candles.last().c

        return lastPrice
    }

    private suspend fun getCurrency(figi: String): String {
        val result = tinkoffRepository.getStockInfo(figi)
        return result.currency
    }

}