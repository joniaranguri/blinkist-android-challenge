package com.blinkslabs.blinkist.android.challenge.ui

import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blinkslabs.blinkist.android.challenge.BlinkistChallengeApplication
import com.blinkslabs.blinkist.android.challenge.R
import com.blinkslabs.blinkist.android.challenge.common.ext.BooksArrangement
import com.blinkslabs.blinkist.android.challenge.common.ext.configureBehaviour
import com.blinkslabs.blinkist.android.challenge.common.ext.setSelectedColor
import com.blinkslabs.blinkist.android.challenge.data.model.BookSection
import com.google.android.material.appbar.AppBarLayout
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class BooksActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener {

    @Inject
    lateinit var booksViewModelFactory: BooksViewModelFactory
    private val viewModel by viewModels<BooksViewModel> { booksViewModelFactory }

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var recyclerAdapter: BookSectionsRecyclerAdapter

    private val subscriptions = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_books)
        initViewComponents()
        configureToolbar()
        (application as BlinkistChallengeApplication)
            .component.inject(this)
        configureBooksList()
        configureViews()
        setUpBooksObserver()
        viewModel.fetchBooks()
    }

    private fun initViewComponents() {
        recyclerView = findViewById(R.id.bookSectionsRecyclerView)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        appBarLayout = findViewById(R.id.appBarLayout)
    }

    private fun configureToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.inflateMenu(R.menu.toolbar_actions)
        setSupportActionBar(toolbar)
    }

    private fun configureBooksList() {
        recyclerAdapter = BookSectionsRecyclerAdapter()
        recyclerView.also {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = recyclerAdapter
        }
    }

    private fun configureViews() {
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshBooks()
        }
        showLoading()
    }

    private fun setUpBooksObserver() {
        viewModel.books().observe(this) { books ->
            showBooks(books)
            hideLoading()
        }
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

    override fun onDestroy() {
        subscriptions.clear()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_actions, menu);
        val alphabeticalMenuItem = menu.findItem(R.id.alphabeticalOrder)
        val weekMenuItem = menu.findItem(R.id.weekOrder)
        subscriptions.add(
            alphabeticalMenuItem.configureBehaviour(this, weekMenuItem) {
                viewModel.updateArrangement(BooksArrangement.ALPHABETICALLY)
            }
        )
        subscriptions.add(
            weekMenuItem.configureBehaviour(this, alphabeticalMenuItem) {
                viewModel.updateArrangement(BooksArrangement.WEEKLY)
            }
        )
        weekMenuItem.setSelectedColor(this)
        return true
    }
}
