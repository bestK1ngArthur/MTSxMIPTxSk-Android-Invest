package com.mipt.android.data.api.responses

import kotlinx.serialization.*

@Serializable
data class APIResult(
    val trackingId: String,
    val status: String,
    @SerialName("payload")
    val data: Map<String, String>
)
