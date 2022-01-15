package com.mipt.android.data

import com.mipt.android.data.api.responses.CandlesResponse
import com.mipt.android.data.api.responses.RegisterResponse
import com.mipt.android.data.api.responses.StockInfoResponse
import com.mipt.android.data.api.responses.UserAccountsResponse
import com.mipt.android.data.api.responses.portfolio.PortfolioResponse
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

interface TinkoffRepository {
    suspend fun registerAccount(): RegisterResponse
    suspend fun removeAccount(brokerAccountId: String)
    suspend fun getUserAccounts(): UserAccountsResponse
    suspend fun getCandles(
        figi: String, interval: String = "month",
        startDate: String = DateTimeFormatter.ISO_INSTANT.format(
            Instant.now().minus(365, ChronoUnit.DAYS)
        ),
        endDate: String = DateTimeFormatter.ISO_INSTANT.format(
            Instant.now()
        )
    ): CandlesResponse

    suspend fun getPortfolio(brokerAccountId: String?): PortfolioResponse
    suspend fun getStockInfo(figi: String): StockInfoResponse


}