package com.blinkslabs.blinkist.android.challenge.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blinkslabs.blinkist.android.challenge.common.getWeekRange
import com.blinkslabs.blinkist.android.challenge.data.BooksService
import com.blinkslabs.blinkist.android.challenge.data.model.Book
import com.blinkslabs.blinkist.android.challenge.data.model.BookSection
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class BooksViewModel @Inject constructor(private val booksService: BooksService) : ViewModel() {

    private val subscriptions = CompositeDisposable()

    private val books = MutableLiveData<List<BookSection>>()

    fun books(): LiveData<List<BookSection>> = books

    fun fetchBooks() {
        viewModelScope.launch {
            try {
                books.value = booksService.getBooks().timeArrangement()
            } catch (e: Exception) {
                Timber.e(e, "while loading data")
            }

        }
    }

    override fun onCleared() {
        subscriptions.clear()
    }
}

private fun List<Book>.timeArrangement(): List<BookSection> {
    return this
        .groupBy { it.publishDate.getWeekRange() }
        .map { BookSection(it.key, it.value) }
}
