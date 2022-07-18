package com.maandraj.auth_impl.domain


import com.maandraj.auth_impl.data.AuthRepo
import com.maandraj.core.data.config.ConfigRepo
import com.maandraj.core.data.model.Account
import com.maandraj.core.data.result.ResultOf
import javax.inject.Inject

class AuthInteractor @Inject constructor(
    private val configRepo: ConfigRepo,
    private val authRepo: AuthRepo,
) {
    suspend fun getAuthUrl(): String = authRepo.getAuthUrl()
    fun isLogged() = configRepo.isLogged()
    fun saveAccount(account: Account): ResultOf<Boolean> {
        return configRepo.saveAccount(account)
    }
}