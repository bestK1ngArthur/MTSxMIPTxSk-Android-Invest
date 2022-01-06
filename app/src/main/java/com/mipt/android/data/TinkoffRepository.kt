package com.mipt.android.data

import com.mipt.android.data.api.responses.RegisterResponse

interface TinkoffRepository {
    suspend fun registerAccount(): RegisterResponse
    suspend fun removeAccount()
}