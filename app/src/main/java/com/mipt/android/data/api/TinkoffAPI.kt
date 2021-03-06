package com.mipt.android.data.api

import com.mipt.android.data.api.requests.RegisterRequest
import com.mipt.android.data.api.responses.*
import com.mipt.android.data.api.responses.portfolio.PortfolioResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TinkoffAPI {
    companion object {
        const val URL_PREFIX = "/openapi/sandbox"
    }

    @POST("$URL_PREFIX/sandbox/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): APIResult<RegisterResponse>

    @POST("$URL_PREFIX/sandbox/remove")
    suspend fun remove(
        @Query("brokerAccountId") brokerAccountId: String
    ): APIResult<VoidResponse>

    @GET("$URL_PREFIX/user/accounts")
    suspend fun getUserAccounts(): APIResult<UserAccountsResponse>

    @GET("$URL_PREFIX/market/candles")
    suspend fun getCandles(
        @Query("figi") figi: String, @Query("from") startDate: String,
        @Query("to") endDate: String, @Query("interval") interval: String,
    ): APIResult<CandlesResponse>

    @GET("$URL_PREFIX/portfolio")
    suspend fun getPortfolio(
        @Query("brokerAccountId") brokerAccountId: String?
    ): APIResult<PortfolioResponse>

    @GET("$URL_PREFIX/market/search/by-figi")
    suspend fun getStockInfo(
        @Query("figi") figi: String
    ): APIResult<StockInfoResponse>
}
