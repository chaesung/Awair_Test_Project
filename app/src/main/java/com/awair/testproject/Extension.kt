package com.awair.testproject

import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

fun String.getDateFromFormat(format: String): Date? {
    return try {
        SimpleDateFormat(format, Locale.US).parse(this)
    } catch (e: Exception) {
        null
    }
}

fun Date.getNewDateWithDiffFormat(format: String): Date? {
    return try {
        SimpleDateFormat(format, Locale.US).run {
            Date(format(this@getNewDateWithDiffFormat))
        }
    } catch (e: Exception) {
        null
    }
}