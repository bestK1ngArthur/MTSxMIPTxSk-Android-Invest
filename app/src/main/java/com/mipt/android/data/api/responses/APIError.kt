package com.mipt.android.data.api.responses

import kotlinx.serialization.Serializable

@Serializable
class APIError(
    var code: String,
    val message: String
)