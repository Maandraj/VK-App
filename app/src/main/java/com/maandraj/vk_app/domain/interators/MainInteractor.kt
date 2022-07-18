package com.maandraj.vk_app.domain.interators

import com.maandraj.core.data.config.ConfigRepo
import com.maandraj.core.data.result.ResultOf
import javax.inject.Inject

class MainInteractor @Inject constructor(
    private val configRepo: ConfigRepo,
) {
    fun isLogged(): Boolean = configRepo.isLogged()
    fun logout() = configRepo.logout()
    fun isSeanceNotExpired(): ResultOf<Boolean> = configRepo.isSeanceNotExpired()
}