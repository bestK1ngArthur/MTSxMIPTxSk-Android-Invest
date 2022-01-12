package com.mipt.android.data.api.responses

import kotlinx.serialization.Serializable

@Serializable
data class CandlesResponse(
    val candles: List<Candle>
) {
    @Serializable
    data class Candle(
        val o: Float,
        val c: Float,
        val h: Float,
        val l: Float,
        val v: Int,
        val time: String,
        val interval: String,
        val figi: String
    )
}
