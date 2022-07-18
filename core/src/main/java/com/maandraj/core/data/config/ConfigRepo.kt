package com.maandraj.core.data.config

import com.maandraj.core.data.model.Account
import com.maandraj.core.data.result.ResultOf


interface ConfigRepo {
    fun isLogged(): Boolean
    fun saveAccount(account: Account): ResultOf<Boolean>
    fun isSeanceNotExpired(): ResultOf<Boolean>
    fun logout()
}
