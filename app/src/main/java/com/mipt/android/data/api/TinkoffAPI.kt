package com.mipt.android.data.api

import com.mipt.android.data.api.responses.*

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface TinkoffAPI {
    @POST("/sandbox/register")
    suspend fun register(
        @Body brokerAccountType: String
    ) : APIResponse<RegisterResponse>

    @POST("/sandbox/remove")
    suspend fun remove(
        @Query("type") brokerAccountId: String
    ) : APIResponse<Void>
}
