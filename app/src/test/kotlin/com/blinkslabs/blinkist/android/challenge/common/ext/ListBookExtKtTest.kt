package com.blinkslabs.blinkist.android.challenge.common.ext

import com.blinkslabs.blinkist.android.challenge.data.model.Book
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.threeten.bp.LocalDate
import java.util.Locale

class ListBookExtKtTest {
    private lateinit var firstBook: Book
    private lateinit var secondBook: Book
    private lateinit var thirdBook: Book
    private lateinit var fourthBook: Book

    @Before
    fun setUp() {
        Locale.setDefault(Locale.ENGLISH)
        firstBook = Book("1", "One book", "Cervantes", LocalDate.of(2023, 12, 31), "url")
        secondBook = Book("2", "Another book", "Garcia Marquez", LocalDate.of(2024, 1, 1), "url")
        thirdBook = Book("3", "Rich dad", "Kiyoski", LocalDate.of(2024, 1, 8), "url")
        fourthBook = Book("4", "Odysseus", "Homer", LocalDate.of(1900, 1, 8), "url")

    }

    @Test
    fun `Same week no matter the year on Weekly arrangement`() {
        val books = listOf(firstBook, secondBook)
        val expectedTitle = "From 31 Dec 2023 - 6 Jan 2024"

        val booksSection = books.applyArrangement(BooksArrangement.WEEKLY)

        assertTrue(booksSection.size == 1)
        assertEquals(expectedTitle, booksSection.first().sectionTitle)
        assertEquals(books, booksSection.first().books)
    }

    @Test
    fun `Same year but different week on Weekly arrangement`() {
        val books = listOf(thirdBook, secondBook)
        val expectedSecondTitle = "From 31 Dec 2023 - 6 Jan 2024"
        val expectedFirstTitle = "From 7 Jan 2024 - 13 Jan 2024"

        val booksSection = books.applyArrangement(BooksArrangement.WEEKLY)

        assertTrue(booksSection.size == 2)
        assertEquals(expectedFirstTitle, booksSection[0].sectionTitle)
        assertEquals(expectedSecondTitle, booksSection[1].sectionTitle)
        assertEquals(listOf(thirdBook), booksSection[0].books)
        assertEquals(listOf(secondBook), booksSection[1].books)
    }

    @Test
    fun `Same letter in same section on Alphabetical arrangement`() {
        val books = listOf(firstBook, fourthBook)
        val expectedTitle = "O"

        val booksSection = books.applyArrangement(BooksArrangement.ALPHABETICALLY)

        assertTrue(booksSection.size == 1)
        assertEquals(expectedTitle, booksSection.first().sectionTitle)
        assertEquals(books, booksSection.first().books)
    }

    @Test
    fun `Different letter in different section on Alphabetical arrangement`() {
        val books = listOf(firstBook, secondBook)
        val expectedFirstTitle = "A"
        val expectedSecondTitle = "O"

        val booksSection = books.applyArrangement(BooksArrangement.ALPHABETICALLY)


        assertTrue(booksSection.size == 2)
        assertEquals(expectedFirstTitle, booksSection[0].sectionTitle)
        assertEquals(expectedSecondTitle, booksSection[1].sectionTitle)
        assertEquals(listOf(secondBook), booksSection[0].books)
        assertEquals(listOf(firstBook), booksSection[1].books)
    }

}