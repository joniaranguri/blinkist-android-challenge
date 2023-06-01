package com.blinkslabs.blinkist.android.challenge.common.ext

import com.blinkslabs.blinkist.android.challenge.data.model.Book
import com.blinkslabs.blinkist.android.challenge.data.model.BookSection

fun List<Book>.applyArrangement(booksArrangement: BooksArrangement): List<BookSection> =
    if (booksArrangement == BooksArrangement.WEEKLY) this.timeArrangement() else this.alphabeticalArrangement()

fun List<Book>.timeArrangement(): List<BookSection> {
    return this
        .groupBy { it.publishedAt.getWeekRange() }
        .map { BookSection(it.key, it.value) }
        .sortedByDescending { it.books.first().publishedAt }
}

fun List<Book>.alphabeticalArrangement(): List<BookSection> {
    return this
        .groupBy { it.title.first() }
        .map { BookSection(it.key.toString(), it.value) }
        .sortedBy { it.sectionTitle }
}

enum class BooksArrangement {
    WEEKLY,
    ALPHABETICALLY;
}
