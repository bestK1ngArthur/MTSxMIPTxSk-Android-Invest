package com.mipt.android.data.api.responses

import kotlinx.serialization.Serializable

@Serializable
data class UserAccountsResponse(
    val accounts: List<Account>
) {
    @Serializable
    data class Account(
        val brokerAccountType: String,
        val brokerAccountId: String
    )
}