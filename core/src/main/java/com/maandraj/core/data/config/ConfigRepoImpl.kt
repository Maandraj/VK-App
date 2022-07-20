package com.maandraj.core.data.config

import android.content.Context
import android.util.Log
import com.maandraj.core.R
import com.maandraj.core.data.result.ResultOf
import com.vk.api.sdk.VK
import com.vk.dto.common.id.UserId
import javax.inject.Inject

class ConfigRepoImpl @Inject constructor(
    private val context: Context,
) : ConfigRepo {

    override fun isLogged(): Boolean {
        return try {
            VK.isLoggedIn()
        } catch (ex: Exception) {
            Log.e(TAG, ex.message.toString())
            false
        }
    }

    override fun saveAccount(
        context: Context,
        accessToken: String,
        userId: UserId,
        secret: String?,
    ): ResultOf<Boolean> {
        return try {
            VK.saveAccessToken(context = context,
                accessToken = accessToken,
                userId = userId,
                secret = secret)
            ResultOf.Success(true)
        } catch (ex: Exception) {
            Log.e(TAG, ex.message.toString())
            ResultOf.Failure(context.getString(R.string.error_save_account), ex)
        }
    }


    override fun logout(): ResultOf<Boolean> {
        return try {
            VK.logout()
            ResultOf.Success(true)
        } catch (ex: Exception) {
            Log.e(TAG, ex.message.toString())
            ResultOf.Failure(context.getString(R.string.error_unknown), ex)
        }
    }
}

private const val TAG = "ConfigRepo"
