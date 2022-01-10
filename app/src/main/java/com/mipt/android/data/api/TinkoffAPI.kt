package com.mipt.android.data.api

import com.mipt.android.data.api.requests.*
import com.mipt.android.data.api.responses.*
import com.mipt.android.data.api.responses.portfolio.APIResultPortfolio

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
    ) : APIResult

    @POST("$URL_PREFIX/sandbox/remove")
    suspend fun remove(
        @Query("brokerAccountId") brokerAccountId: String
    ) : APIResult

    @GET("$URL_PREFIX/portfolio")
    suspend fun getPortfolio(
        @Query("brokerAccountId") brokerAccountId: String?
    ) : APIResultPortfolio
}
