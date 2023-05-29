package com.blinkslabs.blinkist.android.challenge.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.blinkslabs.blinkist.android.challenge.data.model.Book
import org.threeten.bp.LocalDate

@Database(entities = [Book::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class Database : RoomDatabase() {
    abstract fun booksDao(): BooksDao
}

class DateConverter {
    @TypeConverter
    fun fromString(value: String) = LocalDate.parse(value)

    @TypeConverter
    fun dateToString(date: LocalDate) = date.toString()

}