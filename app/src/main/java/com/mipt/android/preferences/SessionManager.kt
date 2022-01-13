package com.mipt.android.preferences

import android.content.SharedPreferences
import javax.inject.Inject

class SessionManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        const val BROKER_ACCOUNT_ID = "broker_account_id"
        const val FIGI = "figi"
    }

    fun createSession(brokerAccountId: String, figi: String) {
        val editor = sharedPreferences.edit()
        editor.putString(BROKER_ACCOUNT_ID, brokerAccountId)
        editor.putString(FIGI, figi)
        editor.apply()
    }

    fun removeSession() {
        var editor = sharedPreferences.edit()
        editor.remove(BROKER_ACCOUNT_ID)
        editor.remove(FIGI)
        editor.apply()
    }

    fun getBrokerAccountId(): String? {
        return sharedPreferences.getString(BROKER_ACCOUNT_ID, null)
    }

    fun getCandles(): String? {
        return sharedPreferences.getString(FIGI, null)
    }

    fun isSessionExists(): Boolean {
        return sharedPreferences.contains(BROKER_ACCOUNT_ID)
    }
}