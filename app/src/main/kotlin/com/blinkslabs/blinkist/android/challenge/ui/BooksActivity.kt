package com.blinkslabs.blinkist.android.challenge.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blinkslabs.blinkist.android.challenge.BlinkistChallengeApplication
import com.blinkslabs.blinkist.android.challenge.R
import com.blinkslabs.blinkist.android.challenge.data.model.BookSection
import com.google.android.material.appbar.AppBarLayout
import javax.inject.Inject

class BooksActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener {

    private lateinit var appBarLayout: AppBarLayout

    @Inject
    lateinit var booksViewModelFactory: BooksViewModelFactory

    private val viewModel by viewModels<BooksViewModel> { booksViewModelFactory }

    private lateinit var recyclerAdapter: BookSectionsRecyclerAdapter

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_books)
        recyclerView = findViewById(R.id.bookSectionsRecyclerView)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        appBarLayout = findViewById(R.id.appBarLayout)

        (application as BlinkistChallengeApplication).component.inject(this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerAdapter = BookSectionsRecyclerAdapter()
        recyclerView.adapter = recyclerAdapter
        swipeRefreshLayout.setOnRefreshListener { viewModel.refreshBooks() }

        viewModel.books().observe(this) { books ->
            showBooks(books)
            hideLoading()
        }

        showLoading()
        viewModel.fetchBooks()
    }

    private fun showLoading() {
        swipeRefreshLayout.isRefreshing = true
    }

    private fun hideLoading() {
        swipeRefreshLayout.isRefreshing = false
    }

    private fun showBooks(books: List<BookSection>) {
        recyclerAdapter.setItems(books)
        hideLoading()
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, i: Int) {
        swipeRefreshLayout.isEnabled = i == 0
    }

    override fun onResume() {
        super.onResume()
        appBarLayout.addOnOffsetChangedListener(this)
    }

    override fun onPause() {
        super.onPause()
        appBarLayout.removeOnOffsetChangedListener(this)
    }
}
