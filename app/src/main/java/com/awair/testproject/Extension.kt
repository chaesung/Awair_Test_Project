package com.awair.testproject

import java.text.SimpleDateFormat
import java.util.*

fun String.getDateFromFormat(format: String): Date {
    return SimpleDateFormat(format).parse(this)
}