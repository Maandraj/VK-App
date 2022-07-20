package com.maandraj.core.data.result

import java.lang.Exception

sealed class ResultOf<out T> {
    data class Success<out R>(val value: R): ResultOf<R>()
    data class Failure(
        val message: String,
        val exception: Exception? = null
    ): ResultOf<Nothing>()
}