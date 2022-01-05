package com.mipt.android.data

import com.mipt.android.data.api.TinkoffAPI

import javax.inject.Inject

class TinkoffRepositoryImpl @Inject constructor(
    private val api: TinkoffAPI
): TinkoffRepository {
    override suspend fun registerAccount() {
        api.register("TOKEN")
    }

    override suspend fun removeAccount() {
        api.remove("ACCOUNT ID")
    }
}