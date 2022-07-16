package com.maandraj.auth_impl.data

import android.net.Uri
import com.maandraj.core.BuildConfig

interface AuthRepo {
    suspend fun getAuthUrl() : Uri
}

