package com.maandraj.auth_impl.ui.authScreen

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maandraj.auth_impl.domain.AuthInteractor
import com.maandraj.core.data.result.ResultOf
import com.maandraj.core.utils.extensions.asLiveData
import com.vk.dto.common.id.UserId
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

    fun saveAccount(context: Context, accessToken: String, userId: UserId) = viewModelScope.launch {
        _loading.postValue(true)
        val resultSave = authInteractor.saveAccount(context = context,
            accessToken = accessToken,
            userId = userId,
            secret = null)
        _isSaveAccountState.postValue(resultSave)
        _loading.postValue(false)
    }

}

private const val TAG = "AuthVM"