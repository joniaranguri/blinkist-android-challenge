package com.blinkslabs.blinkist.android.challenge.common.ext

import org.threeten.bp.LocalDate
import org.threeten.bp.format.TextStyle
import org.threeten.bp.temporal.WeekFields
import java.util.Locale

fun LocalDate.getWeekRange(): String {
    val firstWeekDay = getSundayDate()
    val lastWeekDay = firstWeekDay.plusDays(6)
    return "From ${firstWeekDay.getReadableDate()} - ${lastWeekDay.getReadableDate()}"
}

fun LocalDate.getMonthName(): String =
    month.getDisplayName(TextStyle.SHORT, Locale.getDefault())

fun LocalDate.getReadableDate() =
    "${this.dayOfMonth} ${this.getMonthName()} ${this.year}"

fun LocalDate.getSundayDate(): LocalDate {
    val daysFromSunday = this.get(WeekFields.SUNDAY_START.dayOfWeek()) - 1L
    return this.minusDays(daysFromSunday)
}