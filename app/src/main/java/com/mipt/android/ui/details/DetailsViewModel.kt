package com.mipt.android.ui.portfolio

import android.graphics.Color
import android.graphics.Paint
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
import android.R

import com.github.mikephil.charting.charts.CandleStickChart
import com.mipt.android.data.api.responses.CandlesResponse


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

    val toast: LiveData<String?>
        get() = _toast
    private val _toast = MutableLiveData<String?>()

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
            candleStickEntry.add(CandleEntry(value, candle.h, candle.l, candle.o, candle.c))
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

    fun setCandleStickChart(){

        viewModelScope.launchWithErrorHandler(block = {
            var figi = "BBG005DXJS36"
            var candleArray = tinkoffRepository.getCandles(figi)
            val candleData = getCandlesData(candleArray)


        }, onError = {
            showToast("Неверные фиги")
        })

//        val candleStickChart: CandleStickChart = findViewById(com.mipt.android.R.id.candle_stick)
//        val xvalue = ArrayList<String>()
//        xvalue.add("2021-01-04T07:00:00Z")
//        xvalue.add("2021-01-05T07:00:00Z")
//        xvalue.add("2021-01-06T07:00:00Z")
//        xvalue.add("2021-01-07T07:00:00Z")
//
//        val candleStickEntry = ArrayList<CandleEntry>()
//        candleStickEntry.add(CandleEntry(0F, 74.405F, 73.055F, 74.135F, 74.33F))
//        candleStickEntry.add(CandleEntry(1F, 74.405F, 73.055F, 74.135F, 74.33F))
//        candleStickEntry.add(CandleEntry(2F, 74.405F, 73.055F, 74.135F, 74.33F))
//        candleStickEntry.add(CandleEntry(3F, 74.405F, 73.055F, 74.135F, 74.33F))
//        candleStickEntry.add(CandleEntry(4F, 74.405F, 73.055F, 74.135F, 74.33F))
//
//
//        val candleDataset = CandleDataSet(candleStickEntry, "first")
//        candleDataset.color = Color.rgb(80, 80, 80)
//        candleDataset.shadowColor = R.color.green
//        candleDataset.shadowWidth = 1f
//        candleDataset.decreasingColor = R.color.red
//        candleDataset.decreasingPaintStyle = Paint.Style.FILL
//        candleDataset.increasingColor = R.color.green
//        candleDataset.increasingPaintStyle = Paint.Style.FILL
//
//
//        val candleData = CandleData(xvalue, candleDataset)
//        candleChart.data = candleData
//        candleChart.set




    }

    private fun showToast(message: String) {
        _toast.postValue(message)
    }
}