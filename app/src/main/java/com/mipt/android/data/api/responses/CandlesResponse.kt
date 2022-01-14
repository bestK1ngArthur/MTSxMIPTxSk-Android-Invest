package com.mipt.android.data.api.responses

import kotlinx.serialization.Serializable

@Serializable
data class CandlesResponse(
    val candles: List<Candle>
) {
    @Serializable
    data class Candle(
        val o: Double,
        val c: Double,
        val h: Double,
        val l: Double,
        val v: Double,
        val time: String,
        val interval: String,
        val figi: String
    )
}
