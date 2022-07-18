package com.maandraj.auth_impl.data

import android.net.Uri
import com.maandraj.auth_impl.BuildConfig.AUTH_URL
import com.maandraj.core.BuildConfig

class AuthRepoImpl : AuthRepo {
    override suspend fun getAuthUrl(): String = Uri.Builder()
        .scheme("https")
        .authority(AUTH_URL)
        .appendPath("authorize")
        .appendQueryParameter("client_id", BuildConfig.CLIENT_ID)
        .appendQueryParameter("redirect_uri", BuildConfig.REDIRECT_URI)
        .appendQueryParameter("display", BuildConfig.DISPLAY)
        .appendQueryParameter("scope", BuildConfig.SCOPE)
        .appendQueryParameter("response_type", BuildConfig.RESPONSE_TYPE)
        .appendQueryParameter("v", BuildConfig.VERSION)
        .build().toString()
}