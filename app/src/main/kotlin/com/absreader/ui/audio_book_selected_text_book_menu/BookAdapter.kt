package com.absreader.ui.audio_book_selected_text_book_menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.absreader.R
import com.absreader.data.network.dto.library_items_data.LibraryFile

class BookAdapter(private var books: List<LibraryFile>) : Adapter<BookViewHolder>() {
    private var filteredBooks: List<LibraryFile> = books

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.book_item, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(filteredBooks[position])
    }

    override fun getItemCount(): Int {
        return filteredBooks.size
    }
}
