package com.mipt.android.data.api.responses

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val brokerAccountType: String,
    val brokerAccountId: String
)