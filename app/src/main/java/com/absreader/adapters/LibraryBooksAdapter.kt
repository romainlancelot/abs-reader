package com.absreader.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.absreader.R
import com.absreader.networks.dto.library_items.Result
import com.absreader.view_holders.LibraryBooksViewHolder

class LibraryBooksAdapter(private val books: List<Result>) : Adapter<LibraryBooksViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryBooksViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.library_book, parent, false)
        return LibraryBooksViewHolder(view)
    }

    override fun onBindViewHolder(holder: LibraryBooksViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount(): Int {
        return books.size
    }


}