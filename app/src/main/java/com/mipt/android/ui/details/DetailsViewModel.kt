package com.mipt.android.ui.details

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.mipt.android.R
import com.mipt.android.data.TinkoffRepository
import com.mipt.android.data.api.responses.UserAccountsResponse
import com.mipt.android.launchWithErrorHandler
import com.mipt.android.ui.details.Figi
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
//import android.R

import com.github.mikephil.charting.charts.CandleStickChart
import com.mipt.android.data.api.responses.CandlesResponse
import com.mipt.android.data.api.responses.portfolio.PortfolioResponse
import dagger.hilt.android.qualifiers.ApplicationContext


class DetailsViewModel @AssistedInject constructor(
    @Assisted private val figi: Figi,
    private val tinkoffRepository: TinkoffRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(figi: Figi): DetailsViewModel
    }

    val stockName: LiveData<String?>
        get() = _stockName
    private var _stockName = MutableLiveData<String?>()

    val stockCurrency: LiveData<String?>
        get() = _stockCurrency
    private var _stockCurrency = MutableLiveData<String?>()

    val lastPrice: LiveData<String?>
        get() = _lastPrice
    private var _lastPrice = MutableLiveData<String?>()

    val candleStickChart: LiveData<CandleData?>
            get() = _candleStickChart
    private var _candleStickChart = MutableLiveData<CandleData?>()

    val candleStickImg: LiveData<CandleStickChart?>
        get() = _candleStickImage
    private var _candleStickImage = MutableLiveData<CandleStickChart?>()

    val toast: LiveData<String?>
        get() = _toast
    private val _toast = MutableLiveData<String?>()


    fun getCandleArray(interval: String="month"){
        viewModelScope.launchWithErrorHandler(block = {
            var candleArray = tinkoffRepository.getCandles(figi.id, interval).candles
            val lastPrice = candleArray.last().c
            val candleData = getCandlesData(candleArray)
            _candleStickChart.postValue(candleData)
            _lastPrice.postValue("$lastPrice")


        }, onError = {
            showToast("Неверные фиги")
        })
    }


    fun getStockInfo(){
        viewModelScope.launchWithErrorHandler(block = {
            val stock = tinkoffRepository.getStockInfo(figi.id)
            var stockName = stock.name
            val stockCurrency = stock.currency
            _stockName.postValue(stockName)
            _stockCurrency.postValue(stockCurrency)
        }, onError = {
            showToast("Неверные фиги")
        })
    }


    fun getCandlesData(candleArray: List<CandlesResponse.Candle>): CandleData {
        val candleSize = candleArray.size
        val xvalue = ArrayList<String>()
        val candleStickEntry = ArrayList<CandleEntry>()

        var value = 0
        for (candle in candleArray) {
            xvalue.add(candle.time)
            candleStickEntry.add(CandleEntry(value, candle.h.toFloat(),
                candle.l.toFloat(), candle.o.toFloat(), candle.c.toFloat()
            ))
            value += 1
        }
        val candleDataset = CandleDataSet(candleStickEntry, candleArray[0].interval)

        candleDataset.color = Color.rgb(80, 80, 80)
        val red = ContextCompat.getColor(context, R.color.iRed)
        val green = ContextCompat.getColor(context, R.color.iGreen)
        candleDataset.shadowColor = green
        candleDataset.shadowWidth = 1f
        candleDataset.decreasingColor = red
        candleDataset.decreasingPaintStyle = Paint.Style.FILL
        candleDataset.increasingColor = green
        candleDataset.increasingPaintStyle = Paint.Style.FILL
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        val candleData = CandleData(candleArray.map{formatter.format(parser.parse(it.time))}, candleDataset)
//        _candleStickImg.data = candleData

        return candleData
    }



    private fun showToast(message: String) {
        _toast.postValue(message)
    }
}