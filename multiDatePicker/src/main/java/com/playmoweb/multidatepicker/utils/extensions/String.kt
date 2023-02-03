package com.playmoweb.multidatepicker.utils.extensions

fun String.firstLetterUppercase(): String {
    return this.replaceFirstChar { it.uppercase() }
}