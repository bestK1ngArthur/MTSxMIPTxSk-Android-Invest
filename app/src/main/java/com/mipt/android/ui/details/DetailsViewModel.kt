package com.mipt.android.ui.portfolio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mipt.android.R
import com.mipt.android.data.TinkoffRepository
import com.mipt.android.data.api.responses.UserAccountsResponse
import com.mipt.android.launchWithErrorHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val tinkoffRepository: TinkoffRepository,
) : ViewModel() {
    val stockName: LiveData<String?>
        get() = _stockName
    private var _stockName = MutableLiveData<String?>()

    val toast: LiveData<String?>
        get() = _toast
    private val _toast = MutableLiveData<String?>()

    fun getStockInfo(){
        viewModelScope.launchWithErrorHandler(block = {
            var figi = "BBG005DXJS36"
            var stockName = tinkoffRepository.getStockInfo(figi).name
            _stockName.postValue(stockName)

        }, onError = {
            showToast("Неверные фиги")
        })
    }
    private fun showToast(message: String) {
        _toast.postValue(message)
    }
}