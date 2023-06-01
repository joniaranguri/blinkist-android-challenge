package com.blinkslabs.blinkist.android.challenge.data.source.local

import androidx.room.TypeConverter
import org.threeten.bp.LocalDate

class DateConverter {
    @TypeConverter
    fun fromString(value: String) = LocalDate.parse(value)


    @TypeConverter
    fun dateToString(date: LocalDate) = date.toString()

}