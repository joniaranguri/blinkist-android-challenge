package com.blinkslabs.blinkist.android.challenge.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.blinkslabs.blinkist.android.challenge.data.BooksRepository
import javax.inject.Inject

class BooksViewModelFactory @Inject constructor(
    private val booksRepository: BooksRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        BooksViewModel(booksRepository) as T
}
