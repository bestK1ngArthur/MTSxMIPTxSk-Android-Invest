package com.mipt.android.data

import android.util.Log
import com.mipt.android.data.api.TinkoffAPI
import com.mipt.android.data.api.requests.RegisterRequest
import com.mipt.android.data.api.responses.RegisterResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

import javax.inject.Inject

class TinkoffRepositoryImpl @Inject constructor(
    private val api: TinkoffAPI
): TinkoffRepository {
    override suspend fun registerAccount(): RegisterResponse {
        val result = api.register(RegisterRequest("Tinkoff"))
        val jsonData = Json.encodeToString(result.data)
        return Json.decodeFromString<RegisterResponse>(jsonData)
    }

    override suspend fun removeAccount(brokerAccountId: String) {
        api.remove(brokerAccountId)
    }

    override suspend fun getPortfolio(brokerAccountId: String?) {
        val result = api.getPortfolio(brokerAccountId)
        Log.d("Admin", result.toString());
    }
}