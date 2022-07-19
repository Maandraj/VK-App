package com.maandraj.auth_impl.ui.authScreen.webclient

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
     * Status error
     */
    ERROR,

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