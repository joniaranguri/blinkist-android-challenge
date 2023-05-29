package com.blinkslabs.blinkist.android.challenge.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blinkslabs.blinkist.android.challenge.common.ext.getWeekRange
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

    private val subscriptions = CompositeDisposable()

    private val books = MutableLiveData<List<BookSection>>()

    fun books(): LiveData<List<BookSection>> = books

    fun refreshBooks() {
        fetchBooks(forceRefresh = true)
    }

    fun fetchBooks(forceRefresh: Boolean = false) {
        subscriptions.add(getBooksDisposable(forceRefresh))
    }

    private fun getBooksDisposable(forceRefresh: Boolean): Disposable {
        return booksRepository.getBooks(forceRefresh)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                sortAndUpdateBooks
            ) {
                Timber.e(it, "while loading data")
            }
    }

    private val sortAndUpdateBooks: (t: List<Book>) -> Unit = {
        books.postValue(it.timeArrangement())
    }

    fun List<Book>.timeArrangement(): List<BookSection> {
        return this
            .groupBy { it.publishDate.getWeekRange() }
            .map { BookSection(it.key, it.value) }
    }

    override fun onCleared() {
        subscriptions.clear()
    }
}
