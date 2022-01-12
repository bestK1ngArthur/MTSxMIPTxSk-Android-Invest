package com.mipt.android.data.api.responses.portfolio

import kotlinx.serialization.Serializable

@Serializable
data class Position (
    val figi: String,
    val ticker: String,
    val instrumentType: String,
    val balance: Int,
    val blocked: Int,
    val lots: Int,
    val name: String
)