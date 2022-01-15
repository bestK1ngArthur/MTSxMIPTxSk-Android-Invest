package com.mipt.android.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class APIResult<APIResponse>(
    val trackingId: String,
    val status: String,
    @SerialName("payload")
    val response: APIResponse?
)
