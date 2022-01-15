package com.mipt.android.data

import com.mipt.android.data.api.TinkoffAPI
import com.mipt.android.data.api.requests.RegisterRequest
import com.mipt.android.data.api.responses.CandlesResponse
import com.mipt.android.data.api.responses.RegisterResponse
import com.mipt.android.data.api.responses.StockInfoResponse
import com.mipt.android.data.api.responses.UserAccountsResponse
import com.mipt.android.data.api.responses.portfolio.PortfolioResponse
import javax.inject.Inject

class TinkoffRepositoryImpl @Inject constructor(
    private val api: TinkoffAPI
) : TinkoffRepository {
    override suspend fun registerAccount(): RegisterResponse {
        val result = api.register(RegisterRequest("Tinkoff"))
        return result.response ?: throw TinkoffRepositoryException("Response is null")
    }

    override suspend fun removeAccount(brokerAccountId: String) {
        api.remove(brokerAccountId)
    }

    override suspend fun getUserAccounts(): UserAccountsResponse {
        val result = api.getUserAccounts()
        return result.response ?: throw TinkoffRepositoryException("Response is null")
    }

    override suspend fun getCandles(
        figi: String,
        interval: String,
        startDate: String,
        endDate: String
    ): CandlesResponse {
//        val endDate = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
//        val startDate = DateTimeFormatter.ISO_INSTANT.format(Instant.now().minus(365, ChronoUnit.DAYS))
        val result = api.getCandles(figi, startDate, endDate, interval)
        return result.response ?: throw TinkoffRepositoryException("Response is null")
    }


    override suspend fun getStockInfo(figi: String): StockInfoResponse {
        val result = api.getStockInfo(figi)
        return result.response ?: throw TinkoffRepositoryException("Response is null")
    }

    override suspend fun getPortfolio(brokerAccountId: String?): PortfolioResponse {
        val result = api.getPortfolio(brokerAccountId)

        return result.response ?: throw TinkoffRepositoryException("Response is null")
    }
}