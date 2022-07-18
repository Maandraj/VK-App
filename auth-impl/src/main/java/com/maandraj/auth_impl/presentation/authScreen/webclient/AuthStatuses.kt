package com.maandraj.auth_impl.presentation.authScreen.webclient

/**
 * Status events authentication
 */
enum class AuthStatus {
    /**
     * Status login authentication page
     */
    AUTH,
    /**
     * Status permission check
     */
    CONFIRM,
    /**
     * Status authentication error
     */
    ERROR_INVALID,
    /**
     * Status authentication user denied
     */
    ERROR_USER_DENIED,
    /**
     * Status blocked the user
     */
    BLOCKED,
    /**
     * Status successful authentication
     */
    SUCCESS
}