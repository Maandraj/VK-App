package com.maandraj.auth_impl.presentation.authScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maandraj.auth_impl.domain.AuthInteractor
import com.maandraj.core.data.model.Account
import com.maandraj.core.data.result.ResultOf
import com.maandraj.core.utils.asLiveData
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthVM @Inject constructor(
    private val authInteractor: AuthInteractor,
) : ViewModel() {


    private val _tokenUri = MutableLiveData<String>()
    val tokenUri = _tokenUri.asLiveData()

    private val _isAuth = MutableLiveData<Boolean>()
    val isAuth = _isAuth.asLiveData()

    private val _loading = MutableLiveData<Boolean>()
    val loading = _loading.asLiveData()

    private val _isSaveAccountState = MutableLiveData<ResultOf<Boolean>>()
    val isSaveAccountState = _isSaveAccountState.asLiveData()


    fun getAuthUrl() = viewModelScope.launch {
        _loading.postValue(true)
        val uri = authInteractor.getAuthUrl()
        _tokenUri.postValue(uri)
        _loading.postValue(false)
    }

    fun isLogged() = viewModelScope.launch {
        _loading.postValue(true)
        _isAuth.postValue(authInteractor.isLogged())
        _loading.postValue(false)
    }
    fun authUrlClear() = viewModelScope.launch {
        _tokenUri.postValue("")
    }
    fun saveAccount(accessToken: String, expiresTime: Long, userId: Int) = viewModelScope.launch {
        _loading.postValue(true)
        val account = Account(
            accessToken = accessToken,
            expiresTime = expiresTime,
            userId = userId
        )
        _isSaveAccountState.postValue(authInteractor.saveAccount(account))
        _loading.postValue(false)
    }
}