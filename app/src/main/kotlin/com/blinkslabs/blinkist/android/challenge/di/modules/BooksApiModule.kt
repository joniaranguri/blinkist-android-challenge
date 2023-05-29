package com.blinkslabs.blinkist.android.challenge.di.modules

import com.blinkslabs.blinkist.android.challenge.data.source.api.BooksApi
import com.blinkslabs.blinkist.android.challenge.data.source.api.MockBooksApi
import dagger.Binds
import dagger.Module

@Module
interface BooksApiModule {
    @Binds
    fun bindsBooksApi(mockBooksApi: MockBooksApi): BooksApi
}