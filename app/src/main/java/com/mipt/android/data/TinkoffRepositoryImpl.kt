package com.mipt.android.data

import com.mipt.android.data.api.TinkoffAPI
import com.mipt.android.data.api.requests.RegisterRequest
import com.mipt.android.data.api.responses.RegisterResponse
import com.mipt.android.data.api.responses.UserAccountsResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
}