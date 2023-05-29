package com.blinkslabs.blinkist.android.challenge.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import org.threeten.bp.LocalDate

@Entity(
    tableName = "books"
)
data class Book(
    @Json(name = "id")
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @Json(name = "name")
    @ColumnInfo(name = "name")
    val name: String,

    @Json(name = "author")
    @ColumnInfo(name = "author")
    val author: String,

    @Json(name = "publishDate")
    @ColumnInfo(name = "publishDate")
    val publishDate: LocalDate,

    @Json(name = "coverImageUrl")
    @ColumnInfo(name = "coverImageUrl")
    val coverImageUrl: String
)
