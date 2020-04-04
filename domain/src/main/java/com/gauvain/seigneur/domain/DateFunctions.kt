package com.gauvain.seigneur.domain

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

const val SERVER_DATE_FORMAT = "yyyy-MM-dd"
const val DATA_DATE_FORMAT = "dd/MM/yyyy"
const val SERVER_TIME_FORMAT = "$SERVER_DATE_FORMAT'T'HH:mm:ssX"

@SuppressWarnings("CalendarInstanceUsage")
@Throws(ParseException::class)
fun createDate(date: String, format: String):
    Date = SimpleDateFormat(format, Locale.getDefault()).parse(date)

fun String.toDate(format: String): Date = createDate(this, format)

fun Date.formatTo(format: String): String =
    SimpleDateFormat(format, Locale.getDefault()).format(this)