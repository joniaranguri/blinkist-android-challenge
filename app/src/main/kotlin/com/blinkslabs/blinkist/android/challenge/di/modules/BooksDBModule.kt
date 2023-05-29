package com.blinkslabs.blinkist.android.challenge.di.modules

import android.content.Context
import androidx.room.Room
import com.blinkslabs.blinkist.android.challenge.common.Constants
import com.blinkslabs.blinkist.android.challenge.data.source.local.BooksDao
import com.blinkslabs.blinkist.android.challenge.data.source.local.Database
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class BooksDBModule {

    @Provides
    @Singleton
    fun provideBooksDatabase(app: Context): Database = Room.databaseBuilder(
        app,
        Database::class.java, Constants.DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun provideBooksDao(
        database: Database
    ): BooksDao = database.booksDao()

}
