package com.blinkslabs.blinkist.android.challenge.data

import com.blinkslabs.blinkist.android.challenge.common.NetworkStatus
import com.blinkslabs.blinkist.android.challenge.data.model.Book
import com.blinkslabs.blinkist.android.challenge.data.source.api.BooksApi
import com.blinkslabs.blinkist.android.challenge.data.source.local.BooksDao
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

class BooksRepository @Inject constructor(
    private val booksApi: BooksApi,
    private val booksDao: BooksDao,
    private val networkStatus: NetworkStatus
) {

    fun getBooks(
        forceRefresh: Boolean,
        observableFromApi: Observable<List<Book>>? = getBooksFromAPI(),
        observableFromDb: Observable<List<Book>> = getBooksFromDb()
    ): Observable<List<Book>> =
        if (forceRefresh) observableFromApi ?: observableFromDb
        else observableFromDb.filter {
            it.isNotEmpty()
        }.switchIfEmpty(observableFromApi ?: Observable.fromArray(emptyList()))

    private fun getBooksFromDb(): Observable<List<Book>> = booksDao.getAllBooks()
        .toObservable()
        .doOnNext {
            Timber.tag("*** REPOSITORY DB *** ").e(it.size.toString())
        }

    private fun getBooksFromAPI(): Observable<List<Book>>? =
        if (!networkStatus.isNetworkAvailable()) null
        else booksApi.getAllBooks()
            .doOnNext {
                Timber.tag("*** REPOSITORY API *** ").e(it.size.toString())
                booksDao.saveAllBooks(it)
            }
}
