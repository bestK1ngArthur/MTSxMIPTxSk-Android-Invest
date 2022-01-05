package com.mipt.android.data

interface TinkoffRepository {
    suspend fun registerAccount()
    suspend fun removeAccount()
}