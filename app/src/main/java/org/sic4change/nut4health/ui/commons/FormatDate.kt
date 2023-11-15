package org.sic4change.nut4health.ui.commons

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun formatDateToString(date: Date, pattern: String): String {
    val format = SimpleDateFormat(pattern, Locale.getDefault())
    return format.format(date)
}

fun addMonthToDate(date: Date): Date {
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar.add(Calendar.MONTH, 1)
    return calendar.time
}