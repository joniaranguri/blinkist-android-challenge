package com.blinkslabs.blinkist.android.challenge.data.source.api

import com.blinkslabs.blinkist.android.challenge.data.model.Book
import io.reactivex.Single
import retrofit2.http.GET

interface BooksApi {

    @GET("/books")
    fun getAllBooks(): Single<List<Book>>
}
