package com.mipt.android.data

import com.mipt.android.data.api.TinkoffAPI
import com.mipt.android.data.api.requests.RegisterRequest
import com.mipt.android.data.api.responses.CandlesResponse
import com.mipt.android.data.api.responses.RegisterResponse
import com.mipt.android.data.api.responses.UserAccountsResponse
import com.mipt.android.data.api.responses.portfolio.PortfolioResponse

import javax.inject.Inject

class TinkoffRepositoryImpl @Inject constructor(
    private val api: TinkoffAPI
): TinkoffRepository {
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

    override suspend fun getCandles(figi: String): CandlesResponse {
        val figi = "BBG005DXJS36"
        val startDate = "2020-08-19T18:38:33+03:00"
        val endDate = "2021-08-19T18:38:33+03:00"
        val interval = "day"
        val result = api.getCandles(figi, startDate, endDate, interval)
        return result.response ?: throw TinkoffRepositoryException("Response is null")
    }

    override suspend fun getPortfolio(brokerAccountId: String?): PortfolioResponse {
        val result = api.getPortfolio(brokerAccountId)
        return result.response ?: throw TinkoffRepositoryException("Response is null")
    }
}