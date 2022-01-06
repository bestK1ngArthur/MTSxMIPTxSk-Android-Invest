package com.mipt.android.preferences

import android.content.SharedPreferences
import javax.inject.Inject

class TokenManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        const val API_TOKEN = "api_token"
    }

    fun saveToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString(API_TOKEN, token)
        editor.apply()
    }

    fun removeToken() {
        var editor = sharedPreferences.edit()
        editor.remove(API_TOKEN)
        editor.apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString(API_TOKEN, null)
    }
}