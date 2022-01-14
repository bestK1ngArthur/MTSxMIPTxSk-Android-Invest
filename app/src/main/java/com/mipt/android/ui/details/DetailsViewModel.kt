package com.mipt.android.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mipt.android.data.TinkoffRepository
import com.mipt.android.data.api.responses.UserAccountsResponse
import com.mipt.android.launchWithErrorHandler
import com.mipt.android.ui.details.Figi
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

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
    private fun showToast(message: String) {
        _toast.postValue(message)
    }
}