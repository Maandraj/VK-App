package com.maandraj.core.data.config

import com.maandraj.core.data.model.AuthToken


interface ConfigRepo {
    fun isLogged() : Boolean
    fun saveAccessToken(authToken: AuthToken)
    fun checkTimeToken() : Boolean
    fun logout()
}
