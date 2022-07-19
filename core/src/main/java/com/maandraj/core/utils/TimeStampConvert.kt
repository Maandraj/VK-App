package com.maandraj.core.utils

import android.annotation.SuppressLint
import java.sql.Date
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat", "WeekBasedYear")
fun timeStampConvertDate(timestamp: Long): String {
    val currentDate = Date(timestamp * 1000)
    val dateFormat = SimpleDateFormat("YYYY-MM-dd HH:mm:ss")
    return dateFormat.format(currentDate)
}