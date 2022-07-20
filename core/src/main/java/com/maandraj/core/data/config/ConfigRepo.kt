package com.maandraj.core.data.config

import android.content.Context
import com.maandraj.core.data.result.ResultOf
import com.vk.dto.common.id.UserId


interface ConfigRepo {
    fun isLogged(): Boolean
    fun saveAccount(
        context: Context,
        accessToken: String,
        userId: UserId,
        secret: String? = null,
    ): ResultOf<Boolean>

    fun logout(): ResultOf<Boolean>
}
