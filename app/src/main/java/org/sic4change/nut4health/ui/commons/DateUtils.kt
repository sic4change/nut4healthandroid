package org.sic4change.nut4health.ui.commons

import java.time.Instant
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.util.Date
import java.time.temporal.ChronoUnit

fun getYears(date: Date): Int {
    val today = LocalDate.now()
    val period = Period.between(convertToLocalDate(date), today)
    return period.years
}

fun getMonths(fechaNacimiento: Date): Int {
    val localDateNacimiento = convertToLocalDate(fechaNacimiento)
    val hoy = LocalDate.now()
    val periodo = Period.between(localDateNacimiento, hoy)
    return periodo.months
}

fun convertToLocalDate(date: Date): LocalDate {
    return Instant.ofEpochMilli(date.time)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

fun getMonthsAgo(dateMilliseconds: Long): Long {
    val instantDate = Instant.ofEpochMilli(dateMilliseconds)
    val localDate = instantDate.atZone(ZoneId.systemDefault()).toLocalDate()
    val now = LocalDate.now()
    return ChronoUnit.MONTHS.between(localDate, now)
}
