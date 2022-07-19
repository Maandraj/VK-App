package com.maandraj.auth_impl.domain


import android.content.Context
import com.maandraj.auth_impl.data.AuthRepo
import com.maandraj.core.data.config.ConfigRepo
import com.maandraj.core.data.result.ResultOf
import com.vk.dto.common.id.UserId
import javax.inject.Inject

class AuthInteractor @Inject constructor(
    private val configRepo: ConfigRepo,
    private val authRepo: AuthRepo,
) {
    suspend fun getAuthUrl(): String = authRepo.getAuthUrl()
    fun isLogged() = configRepo.isLogged()
    fun saveAccount(
        context: Context,
        accessToken: String,
        userId: UserId,
        secret: String? = null,
    ): ResultOf<Boolean> {
        return configRepo.saveAccount(context = context,
            accessToken = accessToken,
            userId = userId,
            secret = secret)
    }
}