package com.blinkslabs.blinkist.android.challenge.data

import android.annotation.SuppressLint
import com.blinkslabs.blinkist.android.challenge.common.NetworkStatus
import com.blinkslabs.blinkist.android.challenge.data.model.Book
import com.blinkslabs.blinkist.android.challenge.data.source.api.BooksApi
import com.blinkslabs.blinkist.android.challenge.data.source.local.BooksDao
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.threeten.bp.LocalDate

@RunWith(MockitoJUnitRunner::class)
class BooksRepositoryShould {
    @Mock
    lateinit var booksApi: BooksApi

    @Mock
    lateinit var booksDao: BooksDao

    @Mock
    lateinit var networkStatus: NetworkStatus

    @InjectMocks
    lateinit var booksRepository: BooksRepository

    private val mockApiBooks: List<Book> = listOf(
        spy(Book("1", "Book one", "Jhon", LocalDate.of(2022, 1, 23), "url")),
        spy(Book("2", "Book two", "Michael", LocalDate.of(2022, 1, 23), "url"))
    )

    private val mockDbBooks: List<Book> = listOf(
        spy(Book("3", "Book three", "Jhon", LocalDate.of(2022, 1, 23), "url")),
        spy(Book("4", "Book four", "Michael", LocalDate.of(2022, 1, 23), "url"))
    )

    @SuppressLint("CheckResult")
    @Test
    fun `Call books books DB and Api when getBooks() is called with internet, and forceRefresh`() {
        givenASuccessfulBooksCallWithInternet()

        booksRepository.getBooks(forceRefresh = true).test()

        verify(booksApi, times(1)).getAllBooks()
        verify(booksDao, times(1)).getAllBooks()
    }

    @Test
    fun `Return books from Api when getBooks() is successful with internet, and forceRefresh`() {
        givenASuccessfulBooksCallWithInternet()

        val booksObservable = booksRepository.getBooks(forceRefresh = true)

        booksObservable.test()
            .assertComplete()
            .assertNoErrors()
            .assertValue(mockApiBooks)
    }

    @Test
    fun `Call books DB, and don't call API  when getBooks() is called with internet`() {
        givenASuccessfulBooksCallWithInternet()

        booksRepository.getBooks()

        verify(booksDao, times(1)).getAllBooks()
        verify(booksApi, never()).getAllBooks()
    }

    @Test
    fun `Return books from DB when getBooks() is successful with internet`() {
        givenASuccessfulBooksCallWithInternet()

        val booksObservable = booksRepository.getBooks()

        booksObservable.test()
            .assertComplete()
            .assertNoErrors()
            .assertValue(mockDbBooks)
    }

    @SuppressLint("CheckResult")
    @Test
    fun `Call books DB, but if empty, also call API  when getBooks() is called with internet`() {
        givenASuccessfulBooksCallWithInternet(emptyList())

        booksRepository.getBooks().test()

        verify(booksDao, times(1)).getAllBooks()
        verify(booksApi, times(1)).getAllBooks()
    }

    @Test
    fun `Return books from API when getBooks() is successful and DB empty, with internet`() {
        givenASuccessfulBooksCallWithInternet(emptyList())

        val booksObservable = booksRepository.getBooks()

        booksObservable.test()
            .assertComplete()
            .assertNoErrors()
            .assertValue(mockApiBooks)
    }

    @Test
    fun `Call books DB, and don't call API when getBooks() is called without internet`() {
        givenASuccessfulBooksCallWithoutInternet()

        booksRepository.getBooks()

        verify(booksApi, never()).getAllBooks()
        verify(booksDao, times(1)).getAllBooks()
    }

    @Test
    fun `Return books from DB when getBooks() is successful without internet`() {
        givenASuccessfulBooksCallWithInternet()

        val booksObservable = booksRepository.getBooks()

        booksObservable.test()
            .assertComplete()
            .assertNoErrors()
            .assertValue(mockDbBooks)
    }

    @Test
    fun `Return empty list when getBooks() is successful and DB empty, without internet`() {
        givenASuccessfulBooksCallWithoutInternet(emptyList())

        val booksObservable = booksRepository.getBooks()

        booksObservable.test()
            .assertComplete()
            .assertNoErrors()
            .assertValue(emptyList())
    }

    @Test()
    fun `Propagate exception when getBooks() is unsuccessful`() {
        val customError = RuntimeException("test")
        givenAnUnsuccessfulBooksApiCallWithException(customError)

        val booksObservable = booksRepository.getBooks(true)

        booksObservable.test()
            .assertNotComplete()
            .assertError(customError)

    }

    private fun givenASuccessfulBooksCallWithInternet(dbResult: List<Book> = mockDbBooks) {
        networkStatus.mockInternetAvailable()
        booksApi.mock()
        booksDao.mock { dbResult }
    }

    private fun givenASuccessfulBooksCallWithoutInternet(dbResult: List<Book> = mockDbBooks) {
        networkStatus.mockInternetAvailable { false }
        booksDao.mock { dbResult }
    }

    private fun givenAnUnsuccessfulBooksApiCallWithException(exception: Throwable) {
        networkStatus.mockInternetAvailable()
        booksDao.mock()
        whenever(booksApi.getAllBooks()).thenThrow(exception)
    }

    private fun BooksApi.mock(books: () -> List<Book> = { mockApiBooks }) {
        whenever(this.getAllBooks()).thenReturn(Single.just(books()))
    }

    private fun BooksDao.mock(books: () -> List<Book> = { mockDbBooks }) {
        whenever(this.getAllBooks()).thenReturn(Single.just(books()))
    }

    private fun NetworkStatus.mockInternetAvailable(available: () -> Boolean = { true }) {
        whenever(this.isNetworkAvailable()).thenReturn(available())
    }
}
