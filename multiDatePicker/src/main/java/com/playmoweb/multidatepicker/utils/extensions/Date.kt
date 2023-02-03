package com.playmoweb.multidatepicker.utils.extensions

import java.text.SimpleDateFormat
import java.util.*

const val ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
const val MONTH_YEAR = "MMMM YYYY"
const val SHORT_DAY = "EEE"

fun Date.toMonthYear(): String {
    return this.asString(MONTH_YEAR).firstLetterUppercase()
}

fun Date.toShortDay(): String {
    return this.asString(SHORT_DAY).uppercase(Locale.getDefault())
}

fun Date.asString(format: String = ISO8601): String {
    return SimpleDateFormat(format, Locale.getDefault()).format(this)
}