package com.mipt.android.data

import com.mipt.android.data.api.responses.CandlesResponse
import com.mipt.android.data.api.responses.RegisterResponse
import com.mipt.android.data.api.responses.StockInfoResponse
import com.mipt.android.data.api.responses.UserAccountsResponse

interface TinkoffRepository {
    suspend fun registerAccount(): RegisterResponse
    suspend fun removeAccount(brokerAccountId: String)
    suspend fun getUserAccounts(): UserAccountsResponse
    suspend fun getCandles(figi: String): CandlesResponse
    suspend fun getStockInfo(figi: String): StockInfoResponse
}