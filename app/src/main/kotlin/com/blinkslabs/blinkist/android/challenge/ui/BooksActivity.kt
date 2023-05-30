package com.blinkslabs.blinkist.android.challenge.ui

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blinkslabs.blinkist.android.challenge.BlinkistChallengeApplication
import com.blinkslabs.blinkist.android.challenge.R
import com.blinkslabs.blinkist.android.challenge.common.NetworkState
import com.blinkslabs.blinkist.android.challenge.common.ext.BooksArrangement
import com.blinkslabs.blinkist.android.challenge.common.ext.configureBehaviour
import com.blinkslabs.blinkist.android.challenge.common.ext.setSelectedColor
import com.blinkslabs.blinkist.android.challenge.data.model.BookSection
import com.blinkslabs.blinkist.android.challenge.ui.adapters.BookSectionsRecyclerAdapter
import com.blinkslabs.blinkist.android.challenge.ui.decorators.RecyclerSectionItemDecoration
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
    private lateinit var failedDescriptionTextView: TextView
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
        setUpObservers()
        viewModel.fetchBooks()
    }

    private fun initViewComponents() {
        recyclerView = findViewById(R.id.bookSectionsRecyclerView)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        appBarLayout = findViewById(R.id.appBarLayout)
        failedDescriptionTextView = findViewById(R.id.failedDescriptionText)
    }

    private fun configureToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.inflateMenu(R.menu.toolbar_actions)
        setSupportActionBar(toolbar)
    }

    private fun configureBooksList() {
        recyclerAdapter = BookSectionsRecyclerAdapter()
        val sectionItemDecoration = RecyclerSectionItemDecoration(
            resources.getDimensionPixelSize(R.dimen.recycler_section_header_height),
            recyclerAdapter
        )
        recyclerView.addItemDecoration(sectionItemDecoration)
        recyclerView.also {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = recyclerAdapter
        }
    }


    private fun configureViews() {
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshBooks()
        }
    }

    private fun setUpObservers() {
        viewModel.books().observe(this) { books ->
            showBooks(books)
        }
        viewModel.networkState().observe(this) {
            swipeRefreshLayout.isRefreshing = it == NetworkState.LOADING
            if (it == NetworkState.ERROR || it == NetworkState.NO_RESULTS) {
                failedDescriptionTextView.apply {
                    visibility = View.VISIBLE
                    text = it.msg
                }
            } else failedDescriptionTextView.visibility = View.GONE
        }
    }

    private fun showBooks(books: List<BookSection>) {
        recyclerView.invalidateItemDecorations()
        recyclerAdapter.setItems(books)
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
