package com.mipt.android.data.api.requests

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val brokerAccountType: String
)