package com.maandraj.vk_app.ui.main.activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maandraj.core.data.result.ResultOf
import com.maandraj.core.utils.extensions.asLiveData
import com.maandraj.vk_app.domain.interaсtor.MainInteractor
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityVM @Inject constructor(
    private val mainInteractor: MainInteractor,
) : ViewModel() {

    private val _isLogout = MutableLiveData<ResultOf<Boolean>>()
    val isLogout = _isLogout.asLiveData()

    fun isLogged() = mainInteractor.isLogged()

    fun logout() = viewModelScope.launch {
        val resultLogout = mainInteractor.logout()
        _isLogout.postValue(resultLogout)
    }
}
