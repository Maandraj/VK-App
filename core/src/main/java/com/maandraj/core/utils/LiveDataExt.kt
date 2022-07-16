package com.maandraj.core.utils

import androidx.lifecycle.LiveData

fun <T> LiveData<T>.asLiveData() : LiveData<T> = this