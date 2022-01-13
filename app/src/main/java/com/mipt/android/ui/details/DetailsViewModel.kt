package com.mipt.android.ui.portfolio

import android.graphics.Color
import android.graphics.Paint
import android.util.Log
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


class DetailsViewModel @AssistedInject constructor(
    @Assisted private val figi: Figi,
    private val tinkoffRepository: TinkoffRepository,
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(figi: Figi): DetailsViewModel
    }

    val stockName: LiveData<String?>
        get() = _stockName
    private var _stockName = MutableLiveData<String?>()

    val candleStickChart: LiveData<CandleData?>
            get() = _candleStickChart
    private var _candleStickChart = MutableLiveData<CandleData?>()

    val toast: LiveData<String?>
        get() = _toast
    private val _toast = MutableLiveData<String?>()


    fun getCandleArray(){
        viewModelScope.launchWithErrorHandler(block = {
            var candleArray = tinkoffRepository.getCandles(figi.id).candles
            val candleData = getCandlesData(candleArray)
            _candleStickChart.postValue(candleData)
            val xvalues = candleArray.map{it.time}
            Log.d("time", xvalues.toString())

        }, onError = {
            showToast("Неверные фиги")
        })
    }


    fun getStockInfo(){
        viewModelScope.launchWithErrorHandler(block = {
            var stockName = tinkoffRepository.getStockInfo(figi.id).name
            _stockName.postValue(stockName)
        }, onError = {
            showToast("Неверные фиги")
        })
    }


    fun getCandlesData(candleArray: List<CandlesResponse.Candle>): CandleData {
        val candleSize = candleArray.size
        val xvalue = ArrayList<String>()
        val candleStickEntry = ArrayList<CandleEntry>()

        var value = 0F
        for (candle in candleArray) {
            xvalue.add(candle.time)
            candleStickEntry.add(CandleEntry(value, candle.h.toFloat(),
                candle.l.toFloat(), candle.o.toFloat(), candle.c.toFloat()
            ))
            value += 1
        }
        val candleDataset = CandleDataSet(candleStickEntry, candleArray[0].figi)

        candleDataset.color = Color.rgb(80, 80, 80)
        val red = Color.rgb(255, 0,0)
        val green = Color.rgb(0, 255,0)
        candleDataset.shadowColor = green
        candleDataset.shadowWidth = 1f
        candleDataset.decreasingColor = red
        candleDataset.decreasingPaintStyle = Paint.Style.FILL
        candleDataset.increasingColor = green
        candleDataset.increasingPaintStyle = Paint.Style.FILL
        val candleData = CandleData(candleDataset)
        return candleData
    }



    private fun showToast(message: String) {
        _toast.postValue(message)
    }
}