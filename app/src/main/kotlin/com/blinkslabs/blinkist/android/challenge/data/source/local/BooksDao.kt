package com.blinkslabs.blinkist.android.challenge.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.blinkslabs.blinkist.android.challenge.data.model.Book
import io.reactivex.Single

@Dao
interface BooksDao {

    @Query("SELECT * FROM books ORDER BY publishDate DESC")
    fun getAllBooks(): Single<List<Book>>

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    fun saveAllBooks(cryptocurrencies: List<Book>)
}