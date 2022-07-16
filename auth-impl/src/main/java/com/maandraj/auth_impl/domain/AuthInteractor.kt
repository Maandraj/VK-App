package com.maandraj.auth_impl.domain


import android.net.Uri
import com.maandraj.auth_impl.data.AuthRepo
import com.maandraj.core.data.model.AuthToken
import com.maandraj.core.data.config.ConfigRepo
import javax.inject.Inject

class AuthInteractor @Inject constructor(
    private val configRepo: ConfigRepo,
    private val authRepo: AuthRepo,
) {
    suspend fun getAuthUri(): Uri = authRepo.getAuthUrl()

    fun saveToken(authToken: AuthToken) {
        configRepo.saveAccessToken(authToken)
    }
}