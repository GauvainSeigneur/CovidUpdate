package com.gauvain.seigneur.presentation.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.formatTo(format: String): String =
    SimpleDateFormat(format, Locale.getDefault()).format(this)

@SuppressWarnings("CalendarInstanceUsage")
fun Date.addDay(nbDays: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.DATE, nbDays)
    return calendar.time
}