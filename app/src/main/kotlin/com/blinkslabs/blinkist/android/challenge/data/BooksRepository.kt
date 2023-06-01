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
        forceRefresh: Boolean = false
    ): Observable<List<Book>> {
        val observableFromDb = getBooksFromDb()
        return if (forceRefresh) getBooksFromAPI() ?: observableFromDb
        else observableFromDb.filter {
            it.isNotEmpty()
        }.switchIfEmpty(getBooksFromAPI() ?: Observable.fromArray(emptyList()))
    }

    private fun getBooksFromDb(): Observable<List<Book>> = booksDao.getAllBooks()
        .toObservable()
        .doOnNext {
            Timber.i("Getting %s elements from DB", it.size.toString())
        }

    private fun getBooksFromAPI(): Observable<List<Book>>? =
        if (!networkStatus.isNetworkAvailable()) null
        else Observable.defer {
            booksApi.getAllBooks().toObservable()
        }.doOnNext {
            Timber.i("Getting %s elements from server", it.size.toString())
            booksDao.saveAllBooks(it)
        }
}
