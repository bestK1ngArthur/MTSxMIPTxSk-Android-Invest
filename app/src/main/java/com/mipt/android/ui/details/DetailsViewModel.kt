package com.mipt.android.ui.details

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.icu.text.SimpleDateFormat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.mipt.android.R
import com.mipt.android.data.TinkoffRepository
import com.mipt.android.data.api.responses.CandlesResponse
import com.mipt.android.launchWithErrorHandler
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList

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

    val fromDate: LiveData<Date>
        get() = _fromDate
    private val _fromDate = MutableLiveData<Date>()

    val toDate: LiveData<Date>
        get() = _toDate
    private val _toDate = MutableLiveData<Date>()

    init {
        _fromDate.postValue(Date.from(Instant.now().minus(365, ChronoUnit.DAYS)))
        _toDate.postValue(Date.from(Instant.now()))
    }

    fun getCandleArray(interval: String = "month") {
        val startDate = fromDate.value ?: Date.from(Instant.now().minus(365, ChronoUnit.DAYS))
        val endDate = toDate.value ?: Date.from(Instant.now())

        getCandleArray(
            interval,
            startInstant = startDate.toInstant(),
            endInstant = endDate.toInstant()
        )
    }

    fun getStockInfo() {
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
        val xvalue = ArrayList<String>()
        val candleStickEntry = ArrayList<CandleEntry>()

        var value = 0
        for (candle in candleArray) {
            xvalue.add(candle.time)
            candleStickEntry.add(
                CandleEntry(
                    value, candle.h.toFloat(),
                    candle.l.toFloat(), candle.o.toFloat(), candle.c.toFloat()
                )
            )
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
        val candleData =
            CandleData(candleArray.map { formatter.format(parser.parse(it.time)) }, candleDataset)

        return candleData
    }

    fun setFromDate(date: Date) {
        if (!checkDates(date, toDate.value)) {
            showToast("Неверная дата")
            return
        }

        _fromDate.postValue(date)

        getCandleArray()
    }

    fun setToDate(date: Date) {
        if (!checkDates(fromDate.value, date)) {
            showToast("Неверная дата")
            return
        }

        _toDate.postValue(date)

        getCandleArray()
    }

    private fun checkDates(from: Date?, to: Date?): Boolean {
        if (from != null && to != null) {
            return from < to
        }

        return false
    }

    private fun getCandleArray(
        interval: String = "month",
        startInstant: Instant,
        endInstant: Instant
    ) {
        viewModelScope.launchWithErrorHandler(block = {
            val startDate = DateTimeFormatter.ISO_INSTANT.format(startInstant)
            val endDate = DateTimeFormatter.ISO_INSTANT.format(endInstant)

            var candleArray =
                tinkoffRepository.getCandles(figi.id, interval, startDate, endDate).candles
            val lastPrice = candleArray.last().c
            val candleData = getCandlesData(candleArray)
            _candleStickChart.postValue(candleData)
            _lastPrice.postValue("$lastPrice")
        }, onError = {
            showToast("Неверные фиги")
        })
    }

    private fun showToast(message: String) {
        _toast.postValue(message)
    }
}