package com.maandraj.auth_impl.data

interface AuthRepo {
    suspend fun getAuthUrl(): String
}

