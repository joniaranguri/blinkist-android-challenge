package com.blinkslabs.blinkist.android.challenge.data.source.api

import com.blinkslabs.blinkist.android.challenge.data.model.Book
import io.reactivex.Observable

interface BooksApi {

    fun getAllBooks(): Observable<List<Book>>
}
