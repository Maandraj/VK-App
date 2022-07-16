package com.maandraj.core.data.config

import android.content.SharedPreferences
import com.google.gson.Gson
import com.maandraj.core.data.model.AuthToken
import javax.inject.Inject

class ConfigRepoImpl @Inject constructor(
    private val sharedPref: SharedPreferences,
) : ConfigRepo {

    override fun isLogged() : Boolean {
        return sharedPref.contains(TOKEN)
    }
    override fun saveAccessToken(authToken: AuthToken) {
        val authTokenJson = Gson().toJson(authToken)
        sharedPref.edit().putString(TOKEN, authTokenJson).apply()
    }

    override fun checkTimeToken() : Boolean {
        val tokenJson = sharedPref.getString(TOKEN,null) ?: return false
        val token = Gson().fromJson(tokenJson, AuthToken::class.java)
        val currentTime = System.currentTimeMillis() * 1000
        if (currentTime > token.expiresTime) return false
        return true
    }

    override fun logout() {
        sharedPref.edit().remove(TOKEN).apply()
    }
}

private const val TOKEN = "TOKEN"
