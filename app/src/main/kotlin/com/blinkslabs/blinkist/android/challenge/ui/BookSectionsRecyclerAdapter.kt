package com.blinkslabs.blinkist.android.challenge.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.blinkslabs.blinkist.android.challenge.R
import com.blinkslabs.blinkist.android.challenge.data.model.BookSection

class BookSectionsRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = ArrayList<BookSection>()
    private val viewPool = RecycledViewPool()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.book_section_item, parent, false)
        return BookSectionViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val bookHolder = holder as BookSectionViewHolder
        val bookSection = items[position]

        bookHolder.sectionTitleTextView.text = bookSection.sectionTitle

        with(bookHolder.sectionBooksRecyclerView) {
            layoutManager = LinearLayoutManager(
                holder.sectionBooksRecyclerView.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = BookListRecyclerAdapter(bookSection.books)
            setRecycledViewPool(viewPool)
        }
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<BookSection>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    private class BookSectionViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var sectionTitleTextView: TextView = v.findViewById(R.id.sectionTitleTextView)
        var sectionBooksRecyclerView: RecyclerView = v.findViewById(R.id.sectionBooksRecyclerView)
    }
}
