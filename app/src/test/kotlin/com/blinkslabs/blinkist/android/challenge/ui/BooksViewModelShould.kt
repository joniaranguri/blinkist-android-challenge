package com.blinkslabs.blinkist.android.challenge.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.blinkslabs.blinkist.android.challenge.common.ext.BooksArrangement
import com.blinkslabs.blinkist.android.challenge.data.BooksRepository
import com.blinkslabs.blinkist.android.challenge.data.model.Book
import com.blinkslabs.blinkist.android.challenge.data.model.BookSection
import com.blinkslabs.blinkist.android.challenge.util.getOrAwaitValue
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.threeten.bp.LocalDate

@RunWith(MockitoJUnitRunner::class)
class BooksViewModelShould {

    @get:Rule
    val liveDataRule = InstantTaskExecutorRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var booksRepository: BooksRepository

    @InjectMocks
    lateinit var viewModel: BooksViewModel

    private val mockBooks: List<Book> = listOf(
        spy(Book("1", "Book one", "Jhon", LocalDate.of(2022, 1, 23), "url")),
        spy(Book("2", "Book two", "Michael", LocalDate.of(2022, 1, 23), "url"))
    )
    private val mockBookSectionWeekList: List<BookSection> =
        listOf(BookSection(sectionTitle = "From 23 ene. 2022 - 29 ene. 2022", mockBooks))

    private val mockBookSectionWordList: List<BookSection> =
        listOf(BookSection(sectionTitle = "B", mockBooks))

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

    }

    @Test
    fun `Call getBooks() on Repository when fetchBooks() is called and not refresh`() {
        givenASuccessfulBooksRepositoryCall()

        viewModel.fetchBooks()

        verify(booksRepository).getBooks(false)
    }

    @Test
    fun `Call getBooks() on Repository when fetchBooks() is called and is refreshing`() {
        givenASuccessfulBooksRepositoryCall()
        val forceRefresh = true

        viewModel.fetchBooks(forceRefresh)

        verify(booksRepository).getBooks(forceRefresh)
    }

    @Test
    fun `Show books on view when fetchBooks() is successful and arrangement is by week`() {
        givenASuccessfulBooksRepositoryCall()

        viewModel.fetchBooks()

        assertEquals(mockBookSectionWeekList, viewModel.books().getOrAwaitValue())
    }

    @Test
    fun `Show books on view when fetchBooks() is successful and arrangement is by letter`() {
        givenASuccessfulBooksRepositoryCall()
        viewModel.booksArrangement = BooksArrangement.ALPHABETICALLY

        viewModel.fetchBooks()

        assertEquals(mockBookSectionWordList, viewModel.books().getOrAwaitValue())
    }

    @Test
    fun `Call fetchBooks() with forceRefresh flag when refreshBooks()`() {
        givenASuccessfulBooksRepositoryCall()
        val spiedViewModel = spy(viewModel)

        spiedViewModel.refreshBooks()

        verify(spiedViewModel).fetchBooks(true)
    }

    @Test
    fun `Update books on view when updateArrangement()`() {
        givenASuccessfulBooksRepositoryCall()
        viewModel.fetchBooks()
        assertEquals(mockBookSectionWeekList, viewModel.books().getOrAwaitValue())

        viewModel.updateArrangement(BooksArrangement.ALPHABETICALLY)

        assertEquals(mockBookSectionWordList, viewModel.books().getOrAwaitValue())
    }

    private fun givenASuccessfulBooksRepositoryCall() {
        whenever(booksRepository.getBooks(any())).thenReturn(Observable.just(mockBooks))
    }
}
