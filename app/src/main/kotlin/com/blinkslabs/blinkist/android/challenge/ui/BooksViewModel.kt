package com.blinkslabs.blinkist.android.challenge.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blinkslabs.blinkist.android.challenge.common.NetworkState
import com.blinkslabs.blinkist.android.challenge.common.Status
import com.blinkslabs.blinkist.android.challenge.common.ext.BooksArrangement
import com.blinkslabs.blinkist.android.challenge.common.ext.applyArrangement
import com.blinkslabs.blinkist.android.challenge.data.BooksRepository
import com.blinkslabs.blinkist.android.challenge.data.model.Book
import com.blinkslabs.blinkist.android.challenge.data.model.BookSection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class BooksViewModel @Inject constructor(private val booksRepository: BooksRepository) :
    ViewModel() {

    private var booksArrangement = BooksArrangement.WEEKLY
    private val subscriptions = CompositeDisposable()

    private val books = MutableLiveData<List<BookSection>>()
    private val networkState = MutableLiveData<NetworkState>()

    fun books(): LiveData<List<BookSection>> = books
    fun networkState(): LiveData<NetworkState> = networkState

    fun refreshBooks() {
        fetchBooks(forceRefresh = true)
    }

    fun fetchBooks(forceRefresh: Boolean = false) {
        networkState.postValue(NetworkState.LOADING)
        subscriptions.add(getBooksDisposable(forceRefresh))
    }

    private fun getBooksDisposable(forceRefresh: Boolean): Disposable {
        return booksRepository.getBooks(forceRefresh)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (it.isEmpty()) networkState.postValue(
                        NetworkState.NO_RESULTS
                    )
                    else {
                        sortAndUpdateBooks(it)
                        networkState.postValue(NetworkState.LOADED)
                    }
                }

            ) {
                Timber.e(it, "while loading data")
                networkState.postValue(NetworkState.ERROR)
            }
    }

    private fun sortAndUpdateBooks(booksList: List<Book>) {
        books.postValue(booksList.applyArrangement(booksArrangement))
    }

    fun updateArrangement(newBooksArrangement: BooksArrangement) {
        booksArrangement = newBooksArrangement
        val notSortedBookList = books.value?.flatMap { it.books } ?: emptyList()
        sortAndUpdateBooks(notSortedBookList)
    }

    override fun onCleared() {
        subscriptions.clear()
    }
}
