package com.maandraj.core.data.config

import android.content.Context
import android.content.SharedPreferences
import android.webkit.CookieManager
import com.google.gson.Gson
import com.maandraj.core.R
import com.maandraj.core.data.model.Account
import com.maandraj.core.data.result.ResultOf
import javax.inject.Inject

class ConfigRepoImpl @Inject constructor(
    private val sharedPref: SharedPreferences,
    private val context: Context,
) : ConfigRepo {

    override fun isLogged(): Boolean {
        val result =sharedPref.contains(TOKEN)
        return sharedPref.contains(TOKEN)
    }

    override fun saveAccount(account: Account): ResultOf<Boolean> {
        return try {
            val authTokenJson = Gson().toJson(account)
            sharedPref.edit().putString(TOKEN, authTokenJson).apply()
            ResultOf.Success(true)
        } catch (ex: Exception) {
            ResultOf.Failure(context.getString(R.string.error_save_account), ex)
        }
    }

    override fun isSeanceNotExpired(): ResultOf<Boolean> {
        val tokenJson =
            sharedPref.getString(TOKEN, null) ?: return ResultOf.Failure(context.getString(R.string.error_account_time_expired))
        val token = Gson().fromJson(tokenJson, Account::class.java)
        val currentTime = System.currentTimeMillis() / 1000
        if (currentTime > token.expiresTime) return ResultOf.Failure(context.getString(R.string.error_account_time_expired))
        return ResultOf.Success(true)
    }

    override fun logout() {
        CookieManager.getInstance().removeAllCookies(null)
        sharedPref.edit().remove(TOKEN).apply()
    }
}

private const val TOKEN = "TOKEN"
