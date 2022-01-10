package com.mipt.android.data.api.responses.portfolio

import kotlinx.serialization.Serializable

@Serializable
data class Payload (
    val position : List<Position>
)