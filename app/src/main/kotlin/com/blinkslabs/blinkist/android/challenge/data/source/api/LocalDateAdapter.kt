package com.blinkslabs.blinkist.android.challenge.data.source.api

import com.squareup.moshi.FromJson
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class LocalDateAdapter {

    @FromJson
    fun fromJson(value: String) =
        DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(value, LocalDate.FROM)
}
