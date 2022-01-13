package com.mipt.android.data.api.responses

import kotlinx.serialization.Serializable


@Serializable
data class StockInfoResponse(
    val figi: String,
    val ticker: String,
    val isin: String,
    val minPriceIncrement: Float,
    val lot: Int,
    val currency: String,
    val name: String,
    val type: String
)