package com.maandraj.vk_app.ui.main.activity

import androidx.lifecycle.ViewModel
import com.maandraj.vk_app.domain.interators.MainInteractor
import javax.inject.Inject

class MainActivityVM @Inject constructor(
    private val mainInteractor: MainInteractor,
) : ViewModel() {
    fun isLogged() = mainInteractor.isLogged()
    fun isSeanceNotExpired() = mainInteractor.isSeanceNotExpired()
    fun logout() = mainInteractor.logout()
}
