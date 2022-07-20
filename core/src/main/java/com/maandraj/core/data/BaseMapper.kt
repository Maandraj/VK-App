package com.maandraj.core.data

interface BaseMapper<in A, out B> {
    fun map(res: A): B
}