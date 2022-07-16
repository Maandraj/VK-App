package com.maandraj.core.data.model

data class AuthToken(
    val userId: String,
    val accessToken: String,
    val expiresTime: Long,
)
