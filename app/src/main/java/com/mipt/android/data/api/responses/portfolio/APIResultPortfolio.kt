package com.mipt.android.data.api.responses.portfolio

import kotlinx.serialization.Serializable

@Serializable
data class APIResultPortfolio (
    val trackingId: String,
    val status: String,
    val payload: Payload
)