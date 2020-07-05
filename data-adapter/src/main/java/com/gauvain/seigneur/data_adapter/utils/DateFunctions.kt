package com.gauvain.seigneur.data_adapter.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

const val SERVER_DATE_FORMAT = "yyyy-MM-dd"
const val DATA_DATE_FORMAT = "dd/MM/yyyy"
const val CHART_DATE_FORMAT = "dd/MM"
const val SERVER_TIME_FORMAT = "$SERVER_DATE_FORMAT'T'HH:mm:ssX"

@SuppressWarnings("CalendarInstanceUsage")
@Throws(ParseException::class)
fun createDate(date: String, format: String):
    Date = SimpleDateFormat(format, Locale.getDefault()).parse(date)

fun String.toDate(format: String): Date = createDate(this, format)