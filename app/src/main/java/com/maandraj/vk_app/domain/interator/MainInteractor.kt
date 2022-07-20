package com.maandraj.vk_app.domain.interator

import com.maandraj.core.data.config.ConfigRepo
import javax.inject.Inject

class MainInteractor @Inject constructor(
    private val configRepo: ConfigRepo,
) {
    fun isLogged(): Boolean = configRepo.isLogged()
    fun logout() = configRepo.logout()
}